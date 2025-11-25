package com.example.architectcoders.data

import com.example.architectcoders.data.datasource.MoviesLocalDataSource
import com.example.architectcoders.data.datasource.MoviesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach

class MoviesRepository(
    private val regionRepository: RegionRepository,
    private val localDataSource: MoviesLocalDataSource,
    private val remoteDataSource: MoviesRemoteDataSource
) {

    val movies: Flow<List<Movie>> = localDataSource.movies.onEach { localMovies ->
        if(localMovies.isEmpty()){
            val remoteMovies = remoteDataSource.fetchPopularMovies(regionRepository.findLastRegion())
            localDataSource.save(remoteMovies)
        }
    }

    fun findMovieById(id: Int): Flow<Movie> = localDataSource.findMovieById(id)
        .onEach {movie ->
            if(movie == null) {
                val remoteMovie = remoteDataSource.findMovieById(id)
                localDataSource.save(listOf(remoteMovie))
            }
        }
        .filterNotNull()

    suspend fun deleteFindMovieById(id: Int) = localDataSource.deleteFindMovieById(id)
    suspend fun toogleFavorite(movie: Movie) {
        localDataSource.save(listOf(movie.copy(favorite = !movie.favorite)))
    }

}
