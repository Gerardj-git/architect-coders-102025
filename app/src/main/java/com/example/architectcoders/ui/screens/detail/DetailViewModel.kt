package com.example.architectcoders.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architectcoders.data.Movie
import com.example.architectcoders.data.MoviesRepository
import com.example.architectcoders.ui.screens.home.MovieFavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val id: Int,
    private val repositoryFavorite: MovieFavoriteRepository,
    private val repository: MoviesRepository
): ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> get() = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            repository.findMovieById(id).collect {movie ->
                _state.value = UiState(loading = false, movie = movie)
            }
        }
    }
    data class UiState(
        val loading: Boolean = false,
        val movie: Movie? = null
    )

/*conf un canal aparte
    sealed interface UiEvent{
        data class ShowMessage(val message: String): UiEvent
    }
    private val _events = Channel<UiEvent>()
    val events: Flow<UiEvent> = _events.receiveAsFlow()
*/
    fun onFavoriteClicked(){
        state.value.movie?.let {
            viewModelScope.launch {
                repository.toogleFavorite(it)
            }
        }
    }

}
