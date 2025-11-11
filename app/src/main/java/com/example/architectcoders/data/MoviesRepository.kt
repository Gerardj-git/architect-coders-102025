package com.example.architectcoders.data

import com.example.architectcoders.data.datasource.MoviesLocalDataSource
import com.example.architectcoders.data.datasource.MoviesRemoteDataSource

class MoviesRepository(
    private val regionRepository: RegionRepository,
    private val localDataSource: MoviesLocalDataSource,
    private val remoteDataSource: MoviesRemoteDataSource
) {

    suspend fun fetchPopularMovies(): List<Movie> {
        if (localDataSource.isEmpty()){
            val region = regionRepository.findLastRegion()
            val movies = remoteDataSource.fetchPopularMovies(region)
            localDataSource.saveMovies(movies)
        }
        return localDataSource.fetchPopularMovies()
    }

    suspend fun findMovieById(id: Int): Movie {
        if(localDataSource.findMovieById(id) == null){
            val movie = remoteDataSource.findMovieById(id)
            localDataSource.saveMovies(listOf(movie))
        }
        return checkNotNull(localDataSource.findMovieById(id))
    }

}
