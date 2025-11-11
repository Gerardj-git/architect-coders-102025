package com.example.architectcoders.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.architectcoders.App
import com.example.architectcoders.data.MoviesRepository
import com.example.architectcoders.data.RegionRepository
import com.example.architectcoders.data.datasource.LocationDataSource
import com.example.architectcoders.data.datasource.MoviesLocalDataSource
import com.example.architectcoders.data.datasource.MoviesRemoteDataSource
import com.example.architectcoders.data.datasource.RegionDataSource
import com.example.architectcoders.ui.screens.detail.DetailScreen
import com.example.architectcoders.ui.screens.detail.DetailViewModel
import com.example.architectcoders.ui.screens.home.HomeScreen
import com.example.architectcoders.ui.screens.home.HomeViewModel
import com.example.architectcoders.ui.screens.home.MovieFavoriteRepository

//Navigation typada
sealed class NavScreen(val route: String){
    data object Home: NavScreen("home")
    data object Detail: NavScreen("detail/{${NavArgs.MovieId.key}}"){
        fun createRoute(movieId: Int) = "detail/$movieId"
    }
}
enum class NavArgs(val key: String){
    MovieId("movieId")
}

@Composable
fun Navigation(){

    val repositoryFavorite = MovieFavoriteRepository()

    val navController = rememberNavController()
    val app = LocalContext.current.applicationContext as App
    val moviesRepository = MoviesRepository(
        RegionRepository(
            RegionDataSource(
                app,
                LocationDataSource(app)
            )
        ),
        MoviesLocalDataSource(app.db.movieDao()),
        MoviesRemoteDataSource()
    )


    NavHost(navController = navController, startDestination = NavScreen.Home.route){
        composable(route = NavScreen.Home.route){
            HomeScreen(
                repositoryFavorite,
                onClick = {movie ->
                navController.navigate(NavScreen.Detail.createRoute(movie.id))
            },
                viewModel { HomeViewModel(moviesRepository) })
        }
        composable(route = NavScreen.Detail.route,
            arguments = listOf(navArgument(NavArgs.MovieId.key){ type = NavType.IntType })
        ){ backStackEntry ->
            val movieId = requireNotNull( backStackEntry.arguments?.getInt(NavArgs.MovieId.key))
            DetailScreen(
                viewModel{ DetailViewModel(movieId, repositoryFavorite, moviesRepository) },
                onBack = {
                    navController.popBackStack()
                })
        }
    }
}