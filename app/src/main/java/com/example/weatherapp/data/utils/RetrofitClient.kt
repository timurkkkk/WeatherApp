package com.example.weatherapp.data.utils

import com.example.weatherapp.data.api.CitiesApiService
import com.example.weatherapp.data.api.WeatherApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val citiesRetrofit = Retrofit.Builder()
        .baseUrl("https://api.api-ninjas.com/v1/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val weatherRetrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/v1/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val citiesApiService: CitiesApiService = citiesRetrofit.create(CitiesApiService::class.java)
    val weatherApiService: WeatherApiService = weatherRetrofit.create(WeatherApiService::class.java)
}
