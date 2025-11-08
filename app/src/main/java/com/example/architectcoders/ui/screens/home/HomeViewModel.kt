package com.example.architectcoders.ui.screens.home

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.fastFirst
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architectcoders.data.Movie
import com.example.architectcoders.data.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> get() = _state.asStateFlow()

    private val repository = MoviesRepository()
    
    fun onUiReady(region: String){
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            _state.value = UiState(loading = false, movies = repository.fetchPopularMovies(region))
        }
    }

    fun onUiReadyMovie(id: Int){
        viewModelScope.launch {
                _state.value.movie?.let {movie ->
                _state.update {
                    it.copy(movies = _state.value.movies + movie)
                }
            }
            _state.update {it.copy(movie = _state.value.movies.fastFirst { it.id == id })}
            _state.update { it.copy(movies = _state.value.movies.filter { it.id != id })}
            //_state.value = _state.value.copy(movie = repository.findMovieById(id))

        }
    }

    fun updateMovieStatus(
        id: Int,
        isFavorite: Boolean
    ) {
        viewModelScope.launch {
            _state.value.movie?.let {movie ->
                if (movie.id == id) {
                    _state.update {
                        it.copy(moviesFavorite = _state.value.moviesFavorite + movie)
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