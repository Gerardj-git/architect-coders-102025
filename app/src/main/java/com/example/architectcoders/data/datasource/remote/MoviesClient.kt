package com.example.architectcoders.data.datasource.remote

import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import com.example.architectcoders.BuildConfig
object MoviesClient {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor{apiKeyAsQuery(it)}
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
    }

    val instance = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create<MoviesService>()

}

private fun apiKeyAsQuery(chain: Interceptor.Chain) = chain.proceed(
    chain
        .request()
        .newBuilder()
        .url(
            chain.request().url
                .newBuilder()
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build()
        )
        .build()
)