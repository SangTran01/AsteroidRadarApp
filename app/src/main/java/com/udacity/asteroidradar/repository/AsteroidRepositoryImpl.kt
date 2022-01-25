package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidRepositoryImpl(private val database: AsteroidRadarDatabase): AsteroidRepository {

    override suspend fun getAsteroidsFromApi(): NetworkAsteroidContainer {
        return AsteroidApiService.apiService.getAllAsteroids(API_KEY)
    }

    override suspend fun getDailyImageFromApi(): NetworkDailyImage {
        return AsteroidApiService.apiService.getDailyImage(thumbs = true, key = API_KEY)
    }

    override suspend fun refreshData() {
       withContext(Dispatchers.IO) {
           val asteroids = getAsteroidsFromApi()
           database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
       }
    }

    override suspend fun getAsteroidsFromDatabase(): List<Asteroid> {
        return database.asteroidDao.getAllAsteroids()
    }
}