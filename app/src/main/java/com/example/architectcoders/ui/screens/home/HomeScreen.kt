@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.architectcoders.ui.screens.home

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.architectcoders.data.Movie
import com.example.architectcoders.R
import com.example.architectcoders.ui.common.LoadingProgressIndicator
import com.example.architectcoders.ui.common.PermissionRequestEffect
import com.example.architectcoders.ui.theme.ArchitectCodersTheme

@Composable
fun Screen(content: @Composable () -> Unit) {
    ArchitectCodersTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = content
        )
    }
}

@Composable
fun HomeScreen(
    repositoryFavorite: MovieFavoriteRepository,
    onClick: (Movie) -> Unit,
    vm: HomeViewModel
){

    val homeState = rememberHomeState()

    PermissionRequestEffect(permission = Manifest.permission.ACCESS_COARSE_LOCATION){
        vm.onUiReady() }


    val favoritedMovieId by repositoryFavorite.favoriteStatusId.collectAsState()

    LaunchedEffect(favoritedMovieId) {
        favoritedMovieId?.let { id ->
            // Le indica al HomeViewModel que actualice la lista de la UI.
            vm.updateMovieStatus(id, isFavorite = true)

            // 3. **Consume la información** para evitar que se procese dos veces.
            repositoryFavorite.consumeFavoriteMovieId()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    )
    {
        val state by vm.state.collectAsState()
        //var movieId = state.movie

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .padding(8.dp)
        )
        {
            Screen {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = stringResource(id = R.string.app_name))
                            },
                            scrollBehavior = homeState.scrollBehavior
                        )
                    },
                    modifier = Modifier.nestedScroll(homeState.scrollBehavior.nestedScrollConnection),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { padding ->
                    //val state = vm.state

                    if (state.loading) {
                        LoadingProgressIndicator(modifier = Modifier.padding(paddingValues = padding))
                    }

                    LazyHorizontalGrid(
                        rows = GridCells.Adaptive(120.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        //modifier = Modifier.padding(horizontal = 4.dp),
                        contentPadding = padding
                    ) {
                        items(state.movies) { movie ->
                            MovieItem(
                                movie = movie,
                                onClick = {
                                    //onClick(movie)
                                    vm.onUiReadyMovie(movie.id)
                                    //movieId = state.movie
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier
                .height(4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    state.movie?.let { movie ->
                        PanelInformacionMovie(
                            movie = movie,
                            onClick = {
                                onClick(movie)
                            }
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .height(4.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .padding(8.dp)
                )
                {

                    LazyHorizontalGrid(
                        rows = GridCells.Adaptive(120.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        //modifier = Modifier.padding(horizontal = 4.dp),
                        //contentPadding = padding
                    ) {
                        items(state.moviesFavorite) { movie ->
                            MovieItem(
                                movie = movie,
                                onClick = {
                                    onClick(movie)
                                    //vm.onUiReadyMovie(movie.id)
                                    //movieId = state.movie
                                }
                            )
                        }
                    }

                }

            }

        }

    }
}

@Composable
fun MovieItem(movie: Movie, onClick: () -> Unit){
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = movie.poster,
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2 / 3f)
                .clip(MaterialTheme.shapes.small)
        )
        /*
        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            modifier = Modifier
                .padding(8.dp)
        )
        */

    }
}

@Composable
fun PanelInformacionMovie(movie: Movie, onClick: () -> Unit){

    Row (
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = movie.poster,
            contentDescription = movie.title,
            modifier = Modifier
               // .fillMaxWidth()
                //.aspectRatio(2 / 3f)
                .height(120.dp)
                .width(80.dp)
                .clip(MaterialTheme.shapes.small)
        )
        Column {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }
}