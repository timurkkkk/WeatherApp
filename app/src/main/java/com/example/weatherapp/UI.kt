package com.example.weatherapp

import androidx.annotation.ColorInt
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.text.TextStyle
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import java.util.Calendar
import java.util.TimeZone


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailsScreen(
    viewModel: WeatherViewModel,
    cityName: String,
    onBackClick: () -> Unit
) {
    val cityWeather = viewModel.getCityWeather(cityName)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = cityName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (cityWeather != null) {
                CurrentWeatherSection(cityWeather)
                ThreeDayForecastSection(cityWeather)
                HourlyForecastSection(cityWeather)
            } else {
                Text(
                    text = "Данные о погоде для $cityName не найдены",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun CurrentWeatherSection(cityWeather: CityWeather) {

    val currentHour = Calendar.getInstance(TimeZone.getTimeZone("GMT")).get(Calendar.HOUR_OF_DAY) + cityWeather.city.tzoffset
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
private fun WeatherDetailRow(icon: ImageVector, value: String) {
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

@Composable
private fun ThreeDayForecastSection(cityWeather: CityWeather) {
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
private fun HourlyForecastSection(cityWeather: CityWeather) {
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
                current = (hour == Calendar.getInstance(TimeZone.getTimeZone("GMT")).get(Calendar.HOUR_OF_DAY) + cityWeather.city.tzoffset)
            )
        }
    }
}

@Composable
private fun WeatherParameterRow(parameter: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = parameter, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun DayForecastItem(day: String, condition: String, dayTemp: Double, nightTemp: Double) {
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

private fun getWindDirection(degrees: Int): String {
    return when {
        degrees >= 338 || degrees < 23 -> "С"
        degrees < 68 -> "СВ"
        degrees < 113 -> "В"
        degrees < 158 -> "ЮВ"
        degrees < 203 -> "Ю"
        degrees < 248 -> "ЮЗ"
        degrees < 293 -> "З"
        else -> "СЗ"
    }
}

private fun getWeatherCondition(code: Int): String {
    return when (code) {
        0 -> "Ясно"
        1 -> "Преимущественно ясно"
        2 -> "Переменная облачность"
        3 -> "Пасмурно"
        45, 48 -> "Туман"
        51, 53, 55 -> "Морось"
        56, 57 -> "Ледяная морось"
        61, 63, 65 -> "Дождь"
        66, 67 -> "Ледяной дождь"
        71, 73, 75 -> "Снег"
        77 -> "Снежные зерна"
        80, 81, 82 -> "Ливень"
        85, 86 -> "Снегопад"
        95 -> "Гроза"
        96, 99 -> "Гроза с градом"
        else -> "Неизвестно"
    }
}

@Composable
private fun getWeatherIcon(code: Int): ImageVector {
    return when (code) {
        0 -> Icons.Rounded.WbSunny// Ясно
        1 -> Icons.Outlined.WbSunny // Преимущественно ясно
        2 -> Icons.Outlined.FilterDrama // Переменная облачность
        3 -> Icons.Filled.CloudQueue // Пасмурно
        45, 48 -> Icons.Filled.Cloud // Туман
        51, 53, 55 -> Icons.Filled.Grain // Морось
        56, 57 -> Icons.Filled.AcUnit // Ледяная морось
        61, 63, 65 -> Icons.Default.WaterDrop // Дождь
        66, 67 -> Icons.Filled.AcUnit // Ледяной дождь
        71, 73, 75 -> Icons.Filled.AcUnit // Снег
        77 -> Icons.Filled.AcUnit // Снежные зерна
        80, 81, 82 -> Icons.Filled.Thunderstorm // Ливень
        85, 86 -> Icons.Default.AcUnit // Снегопад
        95 -> Icons.Filled.ElectricBolt // Гроза
        96, 99 -> Icons.Filled.ElectricBolt // Гроза с градом
        else -> Icons.Filled.WbSunny // По умолчанию
    }
}

@Composable
private fun HourlyForecastCard(
    time: String,
    temp: Double,
    condition: String,
    windSpeed: Double,
    windDirection: Int,
    current: Boolean = false
) {
    val conditionCode = when (condition) {
        "Ясно" -> 0
        "Преимущественно ясно" -> 1
        "Переменная облачность" -> 2
        "Пасмурно" -> 3
        "Туман" -> 45
        "Морось" -> 51
        "Ледяная морось" -> 56
        "Дождь" -> 61
        "Ледяной дождь" -> 66
        "Снег" -> 71
        "Снежные зерна" -> 77
        "Ливень" -> 80
        "Снегопад" -> 85
        "Гроза" -> 95
        "Гроза с градом" -> 96
        else -> 0
    }

    var cardBorder: BorderStroke? = null
    if (current) cardBorder = BorderStroke(width = 2.dp, color = Color.DarkGray)

    Card(
        modifier = Modifier
            .width(100.dp)
            .padding(end = 8.dp),
        border = cardBorder
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${temp.toInt()}°C",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Icon(
                imageVector = getWeatherIcon(conditionCode),
                contentDescription = condition,
                modifier = Modifier.size(32.dp),
                tint = when (conditionCode) {
                    0, 1 -> Color(0xFFFFC107)
                    95, 96, 99 -> Color(0xFFFF5722)
                    in 51..82 -> Color(0xFF2196F3)
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Air,
                    contentDescription = "Ветер",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "${windSpeed.toInt()} м/с",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Text(
                text = getWindDirection(windDirection),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}


@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    onCityClick: (CityWeather) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = {
            viewModel.loadWeatherForCities(listOf("Moscow", "London", "Paris", "Washington", "Los Angeles"))
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

@Composable
fun WeatherCard(
    cityWeather: CityWeather,
    onClick: () -> Unit
) {
    val currentHour = Calendar.getInstance(TimeZone.getTimeZone("GMT")).get(Calendar.HOUR_OF_DAY) + cityWeather.city.tzoffset

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