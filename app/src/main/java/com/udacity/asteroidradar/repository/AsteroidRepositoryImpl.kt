package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepositoryImpl(private val database: AsteroidRadarDatabase): AsteroidRepository {

    private val sdf = SimpleDateFormat("yyyy-MM-dd")
    private val date = Date()
    private val c1 = Calendar.getInstance();
    private var currentDate: String = sdf.format(date)



    override suspend fun getAsteroidsFromApi(): NetworkAsteroidContainer {
        return AsteroidApiService.apiService.getAllAsteroids(date = currentDate, key = BuildConfig.API_KEY)
    }

    override suspend fun getDailyImageFromApi(): NetworkDailyImage {
        return AsteroidApiService.apiService.getDailyImage(thumbs = true, key = BuildConfig.API_KEY)
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

    override suspend fun getWeeklyAsteroidsFromDatabase(): List<Asteroid> {
        c1.add(Calendar.DAY_OF_YEAR, 7);
        val endDate = sdf.format(c1.time)
        return database.asteroidDao.getWeeklyAsteroids(currentDate, endDate)
    }
}