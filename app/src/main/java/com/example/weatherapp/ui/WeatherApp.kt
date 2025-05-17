package com.example.weatherapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.data.model.cities
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.ui.details.WeatherDetailsScreen
import com.example.weatherapp.ui.list.WeatherScreen
import com.example.weatherapp.ui.viewmodel.WeatherViewModel
import com.example.weatherapp.ui.viewmodel.WeatherViewModelFactory

@Composable
fun WeatherApp() {
    val navController = rememberNavController()
    val repository = WeatherRepository()
    val viewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory(repository)
    )

    LaunchedEffect(Unit) {
        viewModel.loadWeatherForCities(cities)
    }

    NavHost(navController = navController, startDestination = "weatherList") {
        composable("weatherList") {
            WeatherScreen(
                viewModel = viewModel,
                onCityClick = { cityWeather ->
                    navController.navigate("weatherDetails/${cityWeather.city.name}")
                }
            )
        }
        composable("weatherDetails/{cityName}") { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
            WeatherDetailsScreen(
                viewModel = viewModel,
                cityName = cityName,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
