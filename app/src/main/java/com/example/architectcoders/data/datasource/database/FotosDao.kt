package com.example.architectcoders.data.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.architectcoders.data.Foto
import kotlinx.coroutines.flow.Flow

@Dao
interface FotosDao {

    @Query("SELECT * FROM Foto")
    fun listaFotos(): Flow<List<Foto>>

    @Query("SELECT * FROM Foto WHERE id = :id")
    fun getFoto(id: Int): Flow<Foto>

    @Query("SELECT COUNT(1) FROM Foto")
    suspend fun countFotos(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(foto: List<Foto>)

    @Query("DELETE FROM Foto WHERE id = :id")
    suspend fun deleteFoto(id: Int)

}