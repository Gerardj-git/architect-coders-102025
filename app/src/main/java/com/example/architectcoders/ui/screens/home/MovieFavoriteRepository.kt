package com.example.architectcoders.ui.screens.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MovieFavoriteRepository  {

    private val _favoriteStatusId = MutableStateFlow<Int?>(null)
    val favoriteStatusId: StateFlow<Int?> get() = _favoriteStatusId.asStateFlow()

    fun markMovieAsFavorite(movieId: Int?) {
        _favoriteStatusId.value = movieId
    }

    fun consumeFavoriteMovieId() {
        _favoriteStatusId.value = null
    }
}