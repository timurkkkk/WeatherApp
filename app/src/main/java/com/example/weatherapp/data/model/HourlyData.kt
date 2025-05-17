package com.example.weatherapp.data.model

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