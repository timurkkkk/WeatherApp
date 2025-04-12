package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WeatherApp()
                }
            }
        }
    }
}

@Composable
fun WeatherApp() {
    val navController = rememberNavController()
    val viewModel: WeatherViewModel = viewModel()

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

