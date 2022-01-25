package com.udacity.asteroidradar.api

import android.net.Network
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.DatabaseAsteroid


data class NetworkAsteroidContainer(
    @Json(name = "near_earth_objects")
    val data: Map<String, List<NetworkAsteroid>>
)


data class NetworkAsteroid(
    val id: Long,
    @Json(name = "name")
    val codename: String?,
    @Json(name = "close_approach_data")
    val closeApproachData: List<NetworkCloseApproach>?,
    @Json(name = "estimated_diameter")
    val estimatedDiameter: NetworkDiameter?,
    @Json(name = "is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean?,
    @Json(name = "absolute_magnitude_h")
    val absoluteMagnitude: Double?
)


data class NetworkCloseApproach(
    @Json(name = "close_approach_date")
    val closeApproachDate: String?,
    @Json(name = "relative_velocity")
    val relativeVelocity: NetworkRelativeVelocity?,
    @Json(name = "miss_distance")
    val missDistance: NetworkMissDistance?
)


data class NetworkRelativeVelocity(
    @Json(name = "kilometers_per_second")
    val kilometersPerSecond: Double?
)

data class NetworkMissDistance(
    @Json(name = "astronomical")
    val astronomical: Double?
)

data class NetworkDailyImage(
    val url: String,
    @Json(name = "media_type")
    val mediaType: String,
    val title: String
)

data class NetworkDiameter(
    val kilometers: NetworkKilometers?
)

data class NetworkKilometers(
    @Json(name = "estimated_diameter_min")
    val min: Double?,
    @Json(name = "estimated_diameter_max")
    val max: Double?
)


fun NetworkAsteroidContainer.asDomainModel(): List<Asteroid> {
    val asteroids = mutableListOf<Asteroid>()

    data.map {
        for (a in it.value) {
            asteroids.add( Asteroid(
                id = a.id,
                codename = a.codename ?: "",
                closeApproachDate = a.closeApproachData?.get(0)?.closeApproachDate ?: "",
                absoluteMagnitude = a.absoluteMagnitude ?: 0.0,
                estimatedDiameter = a.estimatedDiameter?.kilometers?.max ?: 0.0,
                relativeVelocity = a.closeApproachData?.get(0)?.relativeVelocity?.kilometersPerSecond ?: 0.0,
                distanceFromEarth = a.closeApproachData?.get(0)?.missDistance?.astronomical ?: 0.0,
                isPotentiallyHazardous = a.isPotentiallyHazardous ?: false
            ))
        }
    }

    return asteroids
}

fun NetworkAsteroidContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    val asteroids = mutableListOf<DatabaseAsteroid>()
    data.map {
        for (a in it.value) {
            asteroids.add( DatabaseAsteroid(
                id = a.id,
                codename = a.codename ?: "",
                closeApproachDate = a.closeApproachData?.get(0)?.closeApproachDate ?: "",
                absoluteMagnitude = a.absoluteMagnitude ?: 0.0,
                estimatedDiameter = a.estimatedDiameter?.kilometers?.max ?: 0.0,
                relativeVelocity = a.closeApproachData?.get(0)?.relativeVelocity?.kilometersPerSecond ?: 0.0,
                distanceFromEarth = a.closeApproachData?.get(0)?.missDistance?.astronomical ?: 0.0,
                isPotentiallyHazardous = a.isPotentiallyHazardous ?: false
            ))
        }
    }
    return asteroids.toTypedArray()
}


//fun NetworkAsteroidContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
//    return videos.map {
//        DatabaseAsteroid(
//            id = it.id)
//    }.toTypedArray()
//}

//fun ArrayList<Asteroid>.asDatabaseModel(): Array<DatabaseAsteroids> {
//    return this.map {
//        DatabaseAsteroids(
//            id = it.id,
//            codename = it.codename,
//            closeApproachDate = it.closeApproachData?.get(0)?.closeApproachDate ?: "",
//            relativeVelocity = it.closeApproachData?.get(0)?.relativeVelocity?.kilometersPerSecond
//                ?: 0.0F,
//            missDistance = it.closeApproachData?.get(0)?.distanceFromEarth?.astronomical ?: 0.0F,
//            absoluteMagnitude = it.absoluteMagnitude,
//            minDiameter = it.estimatedDiameter?.kilometers?.minDiameter,
//            maxDiameter = it.estimatedDiameter?.kilometers?.maxDiameter,
//            isPotentiallyHazardous = it.isPotentiallyHazardous
//        )
//    }.toTypedArray()
//}