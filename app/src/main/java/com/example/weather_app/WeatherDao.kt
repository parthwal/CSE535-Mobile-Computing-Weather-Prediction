// WeatherDao.kt
package com.example.weather_app

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeather(weather: Weather)

    @Query("SELECT * FROM weather WHERE date = :date AND year = :year")
    suspend fun getWeatherByDate(date: String, year: String): Weather?

    @Query("DELETE FROM weather")
    suspend fun deleteAllWeatherData()
}
