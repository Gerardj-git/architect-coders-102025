package com.example.architectcoders.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Foto (
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val title: String,
        val overview: String,
        val releaseDate: String,
        val poster: String,
        val favorite: Boolean
)