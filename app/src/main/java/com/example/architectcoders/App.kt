package com.example.architectcoders

import android.app.Application
import androidx.room.Room
import com.example.architectcoders.data.datasource.database.FotosDatabase
import com.example.architectcoders.data.datasource.database.MoviesDatabase

class App: Application() {

    lateinit var db: MoviesDatabase
        private set

    lateinit var db2: FotosDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(this, MoviesDatabase::class.java, "movies8-db")
            .build()

        db2 = Room.databaseBuilder(this, FotosDatabase::class.java, "fotos8-db")
            .build()
    }

}