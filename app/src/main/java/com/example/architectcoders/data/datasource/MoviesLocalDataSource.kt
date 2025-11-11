package com.example.architectcoders.data.datasource

import com.example.architectcoders.data.Movie
import com.example.architectcoders.data.datasource.database.MoviesDao

class MoviesLocalDataSource(private val moviesDao: MoviesDao) {

    suspend fun fetchPopularMovies() = moviesDao.fetchPopularMovies()

    suspend fun findMovieById(id: Int) = moviesDao.findMovieById(id)

    suspend fun isEmpty() = moviesDao.countMovies() == 0

    suspend fun saveMovies(movies: List<Movie>) = moviesDao.saveMovies(movies)

}