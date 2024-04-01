// Weather.kt
package com.example.weather_app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class Weather(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val year: String,
    val maxTemp: Double,
    val minTemp: Double
)
