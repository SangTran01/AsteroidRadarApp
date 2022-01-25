package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NetworkDailyImage
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.repository.AsteroidRepositoryImpl
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import android.net.NetworkCapabilities

import android.net.ConnectivityManager
import android.os.Build


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
            if (isNetworkAvailable(application)) {
                getDailyImage()
                getAsteroids()
            } else {
                _asteroids.value  = repository.getWeeklyAsteroidsFromDatabase()
            }
        }
    }

    private suspend fun getDailyImage() {
        _dailyImage.value = repository.getDailyImageFromApi()
    }

    private suspend fun getAsteroids() {
        repository.refreshData()
        _asteroids.value  = repository.getWeeklyAsteroidsFromDatabase()
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
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