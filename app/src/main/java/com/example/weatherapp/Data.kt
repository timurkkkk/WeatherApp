package com.example.weatherapp

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

val cities = listOf("Moscow", "London", "Paris", "Izhevsk", "Los Angeles");

data class City(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    var tzoffset: Int = 0
)

data class WeatherData(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val hourly: HourlyData
)

data class HourlyData(
    val time: List<String>,
    val temperature_2m: List<Double>,
    val apparent_temperature: List<Double>? = null,
    val weathercode: List<Int>? = null,
    val windspeed_10m: List<Double>? = null,
    val winddirection_10m: List<Int>? = null,
    val relativehumidity_2m: List<Int>? = null,
    val surface_pressure: List<Double>? = null,
    val precipitation_probability: List<Int>? = null,
    val uv_index: List<Double>? = null
)

data class CityWeather(
    val city: City,
    val weather: WeatherData
)

interface CitiesApiService {
    @GET("city")
    @Headers("X-Api-Key: avjarZMvMV04zJdInjv5HA==DmEF1RuG5Cbdd9ao")
    suspend fun getCityCoordinates(
        @Query("name") name: String,
        @Query("country") country: String? = null
    ): Response<List<City>>
}

interface WeatherApiService {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String = "temperature_2m,apparent_temperature,weathercode,windspeed_10m,winddirection_10m,relativehumidity_2m,surface_pressure,precipitation_probability,uv_index",
        @Query("forecast_days") days: Int = 3//,
        //@Query("timezone") timezone: String = "auto"
    ): WeatherData
}

fun getTimezoneOffset(lat: Double, lon: Double): Int {
    return (lon / 15).toInt().coerceIn(-12..14)
}


class WeatherApiClient(
    private val citiesApi: CitiesApiService,
    private val weatherApi: WeatherApiService
) {
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
                        city.tzoffset = getTimezoneOffset(city.latitude, city.longitude)

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

    companion object {
        fun create(): WeatherApiClient {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val citiesRetrofit = Retrofit.Builder()
                .baseUrl("https://api.api-ninjas.com/v1/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            val weatherRetrofit = Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/v1/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            return WeatherApiClient(
                citiesApi = citiesRetrofit.create(CitiesApiService::class.java),
                weatherApi = weatherRetrofit.create(WeatherApiService::class.java)
            )
        }
    }
}

