package com.example.weatherapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _citiesWeather = mutableStateOf<List<CityWeather>>(emptyList())
    val citiesWeather: State<List<CityWeather>> = _citiesWeather

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val apiClient = WeatherApiClient.create()

    fun loadWeatherForCities(cityNames: List<String>) {
        if (_citiesWeather.value.isNotEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = apiClient.getWeatherForCities(cityNames)
                _citiesWeather.value = result
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCityWeather(cityName: String): CityWeather? {
        return _citiesWeather.value.find { it.city.name.equals(cityName, ignoreCase = true) }
    }
}