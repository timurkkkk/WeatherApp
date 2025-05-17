package com.example.weatherapp.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.FilterDrama
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

fun getWindDirection(degrees: Int): String {
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

fun getWeatherCondition(code: Int): String {
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
fun getWeatherIcon(code: Int): ImageVector {
    return when (code) {
        0 -> Icons.Rounded.WbSunny // Ясно
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
