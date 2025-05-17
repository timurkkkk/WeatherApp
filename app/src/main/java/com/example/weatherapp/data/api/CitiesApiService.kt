package com.example.weatherapp.data.api

import com.example.weatherapp.data.model.City
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CitiesApiService {
    @GET("city")
    @Headers("X-Api-Key: avjarZMvMV04zJdInjv5HA==DmEF1RuG5Cbdd9ao")
    suspend fun getCityCoordinates(
        @Query("name") name: String,
        @Query("country") country: String? = null
    ): Response<List<City>>
}
