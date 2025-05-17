package com.example.weatherapp.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
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
import com.example.weatherapp.ui.utils.getWeatherCondition
import com.example.weatherapp.ui.utils.getWeatherIcon
import com.example.weatherapp.ui.utils.getWindDirection

@Composable
fun HourlyForecastCard(
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

    Card(
        modifier = Modifier
            .width(100.dp)
            .padding(end = 8.dp)
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
