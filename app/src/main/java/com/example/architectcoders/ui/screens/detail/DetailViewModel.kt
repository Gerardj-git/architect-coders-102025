package com.example.architectcoders.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architectcoders.Result
import com.example.architectcoders.data.Movie
import com.example.architectcoders.data.MoviesRepository
import com.example.architectcoders.ifSeccess
import com.example.architectcoders.stateAsResultIn
import com.example.architectcoders.ui.screens.home.MovieFavoriteRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    id: Int,
    private val repository: MoviesRepository
): ViewModel() {
    val state: StateFlow<Result<Movie>> = repository.findMovieById(id)
        .stateAsResultIn(scope = viewModelScope)

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
        state.value.ifSeccess {
            viewModelScope.launch {
                repository.toogleFavorite(it)
            }
        }
    }

}
