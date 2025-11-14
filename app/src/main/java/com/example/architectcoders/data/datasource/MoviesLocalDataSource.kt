package com.example.architectcoders.data.datasource

import com.example.architectcoders.data.Movie
import com.example.architectcoders.data.datasource.database.MoviesDao

class MoviesLocalDataSource(private val moviesDao: MoviesDao) {

    val movies = moviesDao.fetchPopularMovies()

    fun findMovieById(id: Int?) = moviesDao.findMovieById(id)

    suspend fun deleteFindMovieById(id: Int) = moviesDao.deleteFindMovieById(id)

    suspend fun isEmpty() = moviesDao.countMovies() == 0

    suspend fun saveMovies(movies: List<Movie>) = moviesDao.saveMovies(movies)

}