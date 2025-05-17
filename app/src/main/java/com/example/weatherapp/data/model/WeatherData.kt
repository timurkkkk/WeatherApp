package com.example.weatherapp.data.model

data class WeatherData(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val hourly: HourlyData
)