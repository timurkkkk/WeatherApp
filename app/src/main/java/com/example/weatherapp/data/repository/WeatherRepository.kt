package com.example.weatherapp.data.repository

import android.util.Log
import com.example.weatherapp.data.api.CitiesApiService
import com.example.weatherapp.data.api.WeatherApiService
import com.example.weatherapp.data.model.City
import com.example.weatherapp.data.model.CityWeather
import com.example.weatherapp.data.utils.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class WeatherRepository {
    private val citiesApi: CitiesApiService = RetrofitClient.citiesApiService
    private val weatherApi: WeatherApiService = RetrofitClient.weatherApiService

    suspend fun getCityCoordinates(cityName: String): City? {
        return try {
            val response = citiesApi.getCityCoordinates(
                name = cityName,
                country = when (cityName.lowercase()) {
                    "moscow" -> "RU"
                    "london" -> "GB"
                    "paris" -> "FR"
                    else -> null
                }
            )

            when {
                response.isSuccessful -> response.body()?.firstOrNull()?.also {
                    Log.d("API", "Found city: ${it.name} (${it.latitude}, ${it.longitude})")
                }
                response.code() == 404 -> {
                    Log.w("API", "City not found: $cityName")
                    null
                }
                else -> {
                    Log.e("API", "API error: ${response.code()} - ${response.errorBody()?.string()}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("API", "Network error for $cityName", e)
            null
        }
    }

    suspend fun getWeatherForCity(city: City): CityWeather {
        return try {
            val weather = weatherApi.getWeather(
                latitude = city.latitude,
                longitude = city.longitude
            )
            CityWeather(city, weather)
        } catch (e: Exception) {
            throw Exception("Failed to fetch weather for ${city.name}: ${e.message}")
        }
    }

    suspend fun getWeatherForCities(cityNames: List<String>): List<CityWeather> {
        println("DEBUG: Starting to fetch weather for $cityNames")
        return coroutineScope {
            cityNames.map { cityName ->
                async {
                    try {
                        println("DEBUG: Fetching city: $cityName")
                        val city = getCityCoordinates(cityName) ?: run {
                            println("DEBUG: City not found: $cityName")
                            return@async null
                        }

                        println("DEBUG: Fetching weather for ${city.name} (${city.latitude},${city.longitude})")
                        val weather = weatherApi.getWeather(city.latitude, city.longitude)
                        CityWeather(city, weather)
                    } catch (e: Exception) {
                        println("DEBUG: Error for $cityName: ${e.message}")
                        null
                    }
                }
            }.awaitAll().filterNotNull().also {
                println("DEBUG: Received ${it.size} cities weather data")
            }
        }
    }
}
