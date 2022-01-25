package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NetworkDailyImage
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.repository.AsteroidRepositoryImpl
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidRadarDatabase.getInstance(application)
    private val repository = AsteroidRepositoryImpl(database)

    private val _dailyImage = MutableLiveData<NetworkDailyImage>()
    val dailyImage: LiveData<NetworkDailyImage>
        get() = _dailyImage

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids : LiveData<List<Asteroid>> get() = _asteroids

    init {
        viewModelScope.launch {
            getDailyImage()
            getAsteroids()
        }
    }

    private suspend fun getDailyImage() {
        _dailyImage.value = repository.getDailyImageFromApi()
    }

    private suspend fun getAsteroids() {
        repository.refreshData()
        _asteroids.value  = repository.getTodaysAsteroidsFromDatabase()
    }
}

class MainViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("wrong view model")
    }

}