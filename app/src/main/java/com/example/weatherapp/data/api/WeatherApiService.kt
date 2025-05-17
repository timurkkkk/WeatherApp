package com.example.weatherapp.data.api

import com.example.weatherapp.data.model.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String = "temperature_2m,apparent_temperature,weathercode,windspeed_10m,winddirection_10m,relativehumidity_2m,surface_pressure,precipitation_probability,uv_index",
        @Query("forecast_days") days: Int = 3
    ): WeatherData
}
