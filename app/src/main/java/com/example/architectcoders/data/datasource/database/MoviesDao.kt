package com.example.architectcoders.data.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.architectcoders.data.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Query("Select * from Movie")
    fun fetchPopularMovies(): Flow<List<Movie>>

    @Query("Select * from Movie where id = :id")
    fun findMovieById(id: Int?): Flow<Movie?>

    @Query("Select count(1) from Movie")
    suspend fun countMovies(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovies(movies: List<Movie>)

    @Query("DELETE FROM Movie WHERE id = :id")
    suspend fun deleteFindMovieById(id: Int)

}