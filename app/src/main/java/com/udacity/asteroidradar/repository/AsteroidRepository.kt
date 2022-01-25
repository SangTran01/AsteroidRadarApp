package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NetworkAsteroidContainer
import com.udacity.asteroidradar.api.NetworkDailyImage


interface AsteroidRepository {

    suspend fun getAsteroidsFromApi(): NetworkAsteroidContainer

    suspend fun getDailyImageFromApi(): NetworkDailyImage

    suspend fun refreshData()

    suspend fun getAsteroidsFromDatabase(): List<Asteroid>
}