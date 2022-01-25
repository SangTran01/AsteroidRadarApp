package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nasa.gov/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()


interface AsteroidApi {
    @GET("neo/rest/v1/feed")
    suspend fun getAllAsteroids(@Query("start_date") date: String, @Query("api_key") key: String): NetworkAsteroidContainer

    @GET("planetary/apod")
    suspend fun getDailyImage(@Query("thumbs") thumbs: Boolean, @Query("api_key") key: String): NetworkDailyImage
}

object AsteroidApiService {
    val apiService: AsteroidApi by lazy { retrofit.create(AsteroidApi::class.java) }
}