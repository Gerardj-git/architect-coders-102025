package com.example.architectcoders.data.datasource

import com.example.architectcoders.data.Foto
import com.example.architectcoders.data.datasource.database.FotosDao

class FotosLocalDataSource(private val fotosDao: FotosDao) {

    val fotos = fotosDao.listaFotos()

    fun getFoto(id: Int) = fotosDao.getFoto(id)

    suspend fun deleteFoto(id: Int) = fotosDao.deleteFoto(id)

    suspend fun isEmpty() = fotosDao.countFotos() == 0

    suspend fun save(fotos: List<Foto>) = fotosDao.save(fotos)

}