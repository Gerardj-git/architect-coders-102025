package com.example.architectcoders.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architectcoders.data.Movie
import com.example.architectcoders.data.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> get() = _state.asStateFlow()

   init{
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
    }

    fun onUiReadyMovie(id: Int){
        viewModelScope.launch {

            _state.value.movie?.let { movie ->
                _state.update {
                    it.copy(movies = _state.value.movies + movie)
                }
            }

            _state.update {
                it.copy(movies = _state.value.movies.filter { movie ->
                    movie.id != id
                }, movie = null)
            }

            repository.findMovieById(id).collect {movie ->
                _state.update {
                    it.copy(movie = movie)
                }
            }

            //repository.deleteFindMovieById(id)



        }

    }

    fun onUiDeleteMovie(){
        viewModelScope.launch {
            val movieToDelete = _state.value.movie?.id ?: return@launch
            _state.update { it.copy(movie = null) }
            repository.deleteFindMovieById(movieToDelete)
        }
    }

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


    data class UiState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val movie: Movie? = null,
        val moviesFavorite: List<Movie> = emptyList()
    )

}