package com.example.architectcoders.data.datasource

import android.app.Application
import android.location.Geocoder
import android.location.Location
import com.example.architectcoders.ui.common.getFromLocationCompat

const val DEFAULT_REGION = "US"
//encargada de tranformar la location en una region
class RegionDataSource(app: Application,private val locationDataSource: LocationDataSource) {
    private val geocoder = Geocoder(app)

    suspend fun findLastRegion(): String =
        locationDataSource.findLastLocation()?.toRegion() ?: DEFAULT_REGION


    private suspend fun Location.toRegion(): String {
        val addresses = geocoder.getFromLocationCompat(latitude, longitude, 1)
        val region = addresses.firstOrNull()?.countryCode
        return region ?: DEFAULT_REGION
    }
}