// WeatherViewModel.kt
package com.example.weather_app

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherViewModel(private val weatherDao: WeatherDao) : ViewModel() {
    suspend fun saveWeatherData(weather: Weather) {
        withContext(Dispatchers.IO) {
            weatherDao.insertWeather(weather)
        }
    }

    suspend fun getWeatherByDate(date: String, year: String): Weather? {
        return withContext(Dispatchers.IO) {
            val weather = weatherDao.getWeatherByDate(date, year) // Pass 'year' parameter here
            // Filter by year locally
            weather?.takeIf { it.year == year }
        }
    }


    suspend fun deleteAllWeatherData() {
        withContext(Dispatchers.IO) {
            weatherDao.deleteAllWeatherData()
        }
    }
}
