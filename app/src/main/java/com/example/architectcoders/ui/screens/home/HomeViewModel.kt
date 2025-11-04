package com.example.architectcoders.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architectcoders.data.Movie
import com.example.architectcoders.data.MoviesRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var state by mutableStateOf(UiState())
        private set

    private val repository = MoviesRepository()

    fun onUiReady(region: String){
        viewModelScope.launch {
            state = UiState(loading = true)
            state = UiState(loading = false, movies = repository.fetchPopularMovies(region))
        }
    }

    fun onUiReadyMovie(id: Int){
        viewModelScope.launch {
            state.movie?.let {movie ->
                state = state.copy(movies = state.movies + movie)
            }
            state = state.copy(movies = state.movies.filter { it.id != id })
            state = state.copy(movie = repository.findMovieById(id))
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val movie: Movie? = null
    )

}