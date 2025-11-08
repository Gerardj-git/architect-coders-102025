package com.example.architectcoders.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.architectcoders.ui.screens.detail.DetailScreen
import com.example.architectcoders.ui.screens.detail.DetailViewModel
import com.example.architectcoders.ui.screens.home.HomeScreen
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

    val repositoryFavorite = remember { MovieFavoriteRepository() }

    val navController = rememberNavController()



    NavHost(navController = navController, startDestination = NavScreen.Home.route){
        composable(route = NavScreen.Home.route){
            HomeScreen(
                repositoryFavorite,
                onClick = {movie ->
                navController.navigate(NavScreen.Detail.createRoute(movie.id))
            })
        }
        composable(route = NavScreen.Detail.route,
            arguments = listOf(navArgument(NavArgs.MovieId.key){ type = NavType.IntType })
        ){ backStackEntry ->
            val movieId = requireNotNull( backStackEntry.arguments?.getInt(NavArgs.MovieId.key))
            DetailScreen(
                viewModel{ DetailViewModel(movieId, repositoryFavorite) },
                onBack = {
                    navController.popBackStack()
                })
        }
    }
}