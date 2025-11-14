package com.example.architectcoders.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architectcoders.data.Movie
import com.example.architectcoders.data.MoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MoviesRepository
) : ViewModel() {
    
    private val uiReady = MutableStateFlow(false)
    private val selectedMovie = MutableStateFlow<Int?>(null)
    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<UiState> = uiReady
        .filter{ it }
        .flatMapLatest { ready ->
            if(ready) {
                selectedMovie.flatMapLatest { id ->
                    val movieCentralFlow = if (id == null) {
                        MutableStateFlow<Movie?>(null)
                    } else {
                        // Si hay ID, buscamos la película en el repositorio.
                        repository.findMovieById(id)
                    }

                    combine(repository.movies, movieCentralFlow) { moviesList, movieCent ->
                        val (listFavorite, listNoFavorite) = moviesList.partition { movie ->
                            movie.favorite
                        }

                        val topList = if (movieCent != null) {
                                            listNoFavorite.filter { it.id != movieCent.id }
                                        } else {
                                            listNoFavorite
                                        }
                        UiState(
                            loading = false,
                            movies = topList,
                            moviesFavorite = listFavorite,
                            movie = movieCent
                        )

                    }
                }
            } else {
                MutableStateFlow(UiState(loading = true))
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState(loading = true)
        )

   fun onUiReady() {
       uiReady.value = true
/*
        viewModelScope.launch {
            _state.update {
                it.copy(loading = true, movies = emptyList(), moviesFavorite = emptyList())
            }
            repository.movies.collect { movies ->

                val (listFavorite, listNoFavorite) = movies.partition {
                    movie -> movie.favorite
                }

                _state.update {
                    it.copy(
                        loading = false,
                        movies = listNoFavorite,
                        moviesFavorite = listFavorite,
                        movie = null
                    )
                }
            }
        }
*/

    }

    fun onMovieClicked(id: Int){
        selectedMovie.value = id
    }

    fun onCentralMovieConsumed() {
        selectedMovie.value = null
    }
    fun onUiDeleteMovie(){
        viewModelScope.launch {
            val movieToDelete = selectedMovie.value ?: return@launch
            repository.deleteFindMovieById(movieToDelete)
            selectedMovie.value = null

        }
    }

    /*
    fun updateMovieStatus(
        id: Int,
        isFavorite: Boolean
    ) {
        viewModelScope.launch {
            _state.value.movie?.let { movie ->
                if (movie.id == id) {
                    _state.update {
                        it.copy(moviesFavorite = _state.value.moviesFavorite + movie, movie = null)
                    }
                }
            }
        }
    }
    */

    data class UiState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val movie: Movie? = null,
        val moviesFavorite: List<Movie> = emptyList()
    )

}