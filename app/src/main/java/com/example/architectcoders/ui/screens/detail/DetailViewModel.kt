package com.example.architectcoders.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architectcoders.data.Movie
import com.example.architectcoders.data.MoviesRepository
import com.example.architectcoders.ui.screens.home.MovieFavoriteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel(
    id: Int,
    private val repositoryFavorite: MovieFavoriteRepository,
    private val repository: MoviesRepository
): ViewModel() {
    val state: StateFlow<UiState> = repository.findMovieById(id)
        .map { movie ->
            UiState(movie = movie)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState(loading = true))

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
