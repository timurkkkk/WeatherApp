package com.example.weatherapp.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.model.CityWeather
import com.example.weatherapp.data.model.cities
import com.example.weatherapp.ui.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    onCityClick: (CityWeather) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            viewModel.loadWeatherForCities(cities)
        }) {
            Text("Обновить")
        }

        when {
            viewModel.isLoading.value -> CircularProgressIndicator()
            viewModel.error.value != null -> Text(viewModel.error.value!!, color = Color.Red)
            else -> WeatherList(viewModel.citiesWeather.value, onCityClick)
        }
    }
}

@Composable
fun WeatherList(
    cities: List<CityWeather>,
    onCityClick: (CityWeather) -> Unit
) {
    LazyColumn {
        items(cities.count()) { index ->
            WeatherCard(cities[index], onClick = { onCityClick(cities[index]) })
        }
    }
}
