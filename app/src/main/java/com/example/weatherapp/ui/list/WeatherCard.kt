package com.example.weatherapp.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.model.CityWeather
import com.example.weatherapp.ui.utils.getWeatherCondition
import com.example.weatherapp.ui.utils.getWeatherIcon
import java.util.Calendar
import java.util.TimeZone

@Composable
fun WeatherCard(
    cityWeather: CityWeather,
    onClick: () -> Unit
) {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    val currentTemp = cityWeather.weather.hourly.temperature_2m.getOrNull(currentHour)?.toInt() ?: 0
    val nightTemp = cityWeather.weather.hourly.temperature_2m.getOrNull((currentHour + 12) % 24)?.toInt() ?: currentTemp
    val weatherCode = cityWeather.weather.hourly.weathercode?.getOrNull(currentHour) ?: 0
    val weatherCondition = getWeatherCondition(weatherCode)
    val weatherIcon = getWeatherIcon(weatherCode)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = cityWeather.city.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = weatherCondition,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "$currentTemp°/$nightTemp°",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = weatherIcon,
                    contentDescription = weatherCondition,
                    modifier = Modifier.size(32.dp),
                    tint = when (weatherCode) {
                        0, 1 -> Color(0xFFFFC107)
                        95, 96, 99 -> Color(0xFFFF5722)
                        in 51..82 -> Color(0xFF2196F3)
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}
