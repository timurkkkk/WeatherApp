package com.example.weatherapp.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.model.CityWeather
import com.example.weatherapp.ui.utils.getWeatherCondition
import com.example.weatherapp.ui.utils.getWindDirection
import java.util.Calendar
import java.util.TimeZone

@Composable
fun CurrentWeatherSection(cityWeather: CityWeather) {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val hourlyData = cityWeather.weather.hourly

    val currentTemp = hourlyData.temperature_2m.getOrNull(currentHour) ?: 0.0
    val currentCondition = getWeatherCondition(hourlyData.weathercode?.getOrNull(currentHour) ?: 0)
    val feelsLike = cityWeather.weather.hourly.apparent_temperature?.getOrNull(currentHour) ?: currentTemp
    val windSpeed = cityWeather.weather.hourly.windspeed_10m?.getOrNull(currentHour) ?: 0.0
    val windDirection = cityWeather.weather.hourly.winddirection_10m?.getOrNull(currentHour) ?: 0
    val humidity = cityWeather.weather.hourly.relativehumidity_2m?.getOrNull(currentHour) ?: 0
    val pressure = ((cityWeather.weather.hourly.surface_pressure?.getOrNull(currentHour) ?: 0).toDouble() * 0.750062).toInt()
    val precipitationProb = cityWeather.weather.hourly.precipitation_probability?.getOrNull(currentHour) ?: 0
    val uvIndex = cityWeather.weather.hourly.uv_index?.getOrNull(currentHour) ?: 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${currentTemp.toInt()}°",
            style = TextStyle(fontSize = 64.sp, fontWeight = FontWeight.Bold),
            fontSize = 64.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = currentCondition,
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        WeatherDetailRow(
            icon = Icons.Default.Thermostat,
            value = "Ощущается как ${feelsLike.toInt()}°"
        )
        WeatherDetailRow(
            icon = Icons.Default.Air,
            value = "Ветер ${windSpeed.toInt()} м/с, ${getWindDirection(windDirection)}"
        )
        WeatherDetailRow(
            icon = Icons.Default.WaterDrop,
            value = "Влажность $humidity%"
        )

        WeatherDetailRow(
            icon = Icons.Default.Speed,
            value = "Давление $pressure мм рт. ст."
        )

        WeatherDetailRow(
            icon = Icons.Default.Cloud,
            value = "Вероятность осадков $precipitationProb%"
        )

        WeatherDetailRow(
            icon = Icons.Default.LightMode,
            value = "УФ индекс $uvIndex"
        )
    }
}

@Composable
fun WeatherDetailRow(icon: ImageVector, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
