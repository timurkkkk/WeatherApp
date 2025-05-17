package com.example.weatherapp.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.model.CityWeather
import com.example.weatherapp.ui.utils.getWeatherCondition
import java.util.Calendar
import java.util.TimeZone

@Composable
fun ThreeDayForecastSection(cityWeather: CityWeather) {
    Text(
        text = "Прогноз на 3 дня",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Column {
        DayForecastItem(
            day = "Сегодня",
            condition = getWeatherCondition(cityWeather.weather.hourly.weathercode?.get(12) ?: 0),
            dayTemp = cityWeather.weather.hourly.temperature_2m[12],
            nightTemp = cityWeather.weather.hourly.temperature_2m[0]
        )
        DayForecastItem(
            day = "Завтра",
            condition = getWeatherCondition(cityWeather.weather.hourly.weathercode?.get(36) ?: 0),
            dayTemp = cityWeather.weather.hourly.temperature_2m[36],
            nightTemp = cityWeather.weather.hourly.temperature_2m[24]
        )
        DayForecastItem(
            day = "Послезавтра",
            condition = getWeatherCondition(cityWeather.weather.hourly.weathercode?.get(60) ?: 0),
            dayTemp = cityWeather.weather.hourly.temperature_2m[60],
            nightTemp = cityWeather.weather.hourly.temperature_2m[48]
        )
    }
}

@Composable
fun HourlyForecastSection(cityWeather: CityWeather) {
    Text(
        text = "Почасовой прогноз",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    LazyRow {
        items(24) { hour ->
            HourlyForecastCard(
                time = cityWeather.weather.hourly.time[hour].split("T").last(),
                temp = cityWeather.weather.hourly.temperature_2m[hour],
                condition = getWeatherCondition(cityWeather.weather.hourly.weathercode?.get(hour) ?: 0),
                windSpeed = cityWeather.weather.hourly.windspeed_10m?.get(hour) ?: 0.0,
                windDirection = cityWeather.weather.hourly.winddirection_10m?.get(hour) ?: 0,
                current = (hour == Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
            )
        }
    }
}

@Composable
fun DayForecastItem(day: String, condition: String, dayTemp: Double, nightTemp: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = day, fontWeight = FontWeight.Bold)
            Text(text = condition)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "День: ${dayTemp}°C")
                Text(text = "Ночь: ${nightTemp}°C")
            }
        }
    }
}
