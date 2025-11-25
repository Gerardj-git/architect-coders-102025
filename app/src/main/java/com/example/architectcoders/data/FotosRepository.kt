package com.example.architectcoders.data

import com.example.architectcoders.data.datasource.FotosLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

class FotosRepository(
    private val localDataSource: FotosLocalDataSource
) {

    val fotos: Flow<List<Foto>> = localDataSource.fotos

    fun getFoto(id: Int): Flow<Foto> = localDataSource.getFoto(id)
        .filterNotNull()

    suspend fun deleteFoto(id: Int) = localDataSource.deleteFoto(id)


}