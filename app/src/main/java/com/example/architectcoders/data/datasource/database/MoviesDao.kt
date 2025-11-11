package com.example.architectcoders.data.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.architectcoders.data.Movie

@Dao
interface MoviesDao {

    @Query("Select * from Movie")
    suspend fun fetchPopularMovies(): List<Movie>

    @Query("Select * from Movie where id = :id")
    suspend fun findMovieById(id: Int): Movie?

    @Query("Select count(1) from Movie")
    suspend fun countMovies(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovies(movies: List<Movie>)



}