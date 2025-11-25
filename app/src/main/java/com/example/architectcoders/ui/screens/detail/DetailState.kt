package com.example.architectcoders.ui.screens.detail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.example.architectcoders.Result
import com.example.architectcoders.data.Movie

@OptIn(ExperimentalMaterial3Api::class)
class DetailState(
    private val state: Result<Movie>,
    val scrollBehavior: TopAppBarScrollBehavior,
    val snackbarHostState: SnackbarHostState
) {
    val movie: Movie?
        get() = (state as? Result.Success)?.data

    val topBarTitle: String
        get() = movie?.title ?: ""

    @Composable
    fun ShowMessageEffect(message: String?, onMessageShown: () -> Unit){
        //suscribirse a los cambios de estado, en este caso STARTED
        //parametros: cambian de valores se lanza
        LaunchedEffect(message) {
            message?.let {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(it)
                onMessageShown()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberDetailState(
    state: Result<Movie>,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): DetailState {
    return remember(state,scrollBehavior, snackbarHostState) { DetailState(state = state, scrollBehavior = scrollBehavior, snackbarHostState = snackbarHostState) }
}