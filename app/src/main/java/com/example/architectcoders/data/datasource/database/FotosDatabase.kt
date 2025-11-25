package com.example.architectcoders.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.architectcoders.data.Foto

@Database(entities = [Foto::class], version = 1, exportSchema = false)
abstract class FotosDatabase: RoomDatabase() {

    abstract fun fotoDao(): FotosDao
}