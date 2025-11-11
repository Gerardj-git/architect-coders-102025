package com.example.architectcoders.data

import com.example.architectcoders.data.datasource.RegionDataSource
class RegionRepository(private val regionDataSource: RegionDataSource) {

    suspend fun findLastRegion(): String = regionDataSource.findLastRegion()

}