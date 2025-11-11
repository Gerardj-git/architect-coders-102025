package com.example.architectcoders.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.architectcoders.data.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun movieDao(): MoviesDao
}