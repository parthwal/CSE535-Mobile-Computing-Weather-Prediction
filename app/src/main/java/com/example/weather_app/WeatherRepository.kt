// WeatherRepository.kt
package com.example.weather_app

import com.example.weather_app.q1_backup.WeatherInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.util.Calendar
import javax.net.ssl.HttpsURLConnection

suspend fun fetchWeatherData(date: String, year: String, weatherViewModel: WeatherViewModel): WeatherInfo {
    val existingWeather = weatherViewModel.getWeatherByDate(date, year)
    existingWeather?.let {
        return WeatherInfo(it.maxTemp, it.minTemp)
    }
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1 // Month is 0-indexed in Calendar

    val apiKey = "8EHXSYEBKG6YUUD8EQZL7E6DJ" // Replace with your actual API key

    return try {
        if (year.toInt() > currentYear || (year.toInt() == currentYear && date.substringBefore('-').toInt() > currentMonth)) {
            // If the year is in the future or the date hasn't occurred yet this year, fetch data from previous years
            val lastTenYears = (currentYear - 10) until currentYear
            val temps = mutableListOf<WeatherInfo>()
            for (yr in lastTenYears) {
                val tempWeather = weatherViewModel.getWeatherByDate(date, yr.toString())
                if (tempWeather != null) {
                    temps.add(WeatherInfo(tempWeather.maxTemp, tempWeather.minTemp))
                } else {
                    val tempApiUrl = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Delhi,IN/$yr-$date?key=$apiKey&include=days&elements=tempmax%2Ctempmin"
                    val tempWeatherInfo = fetchDataFromApi(tempApiUrl)
                    temps.add(tempWeatherInfo)
                    weatherViewModel.saveWeatherData(
                        Weather(
                            date = date,
                            year = yr.toString(),
                            maxTemp = tempWeatherInfo.maxTemp,
                            minTemp = tempWeatherInfo.minTemp
                        )
                    )
                }
            }
            if (temps.isNotEmpty()) {
                val avgMaxTemp = temps.map { it.maxTemp }.average()
                val avgMinTemp = temps.map { it.minTemp }.average()
                val weather = Weather(date = date, year = year, maxTemp = avgMaxTemp, minTemp = avgMinTemp)
                weatherViewModel.saveWeatherData(weather)
                WeatherInfo(avgMaxTemp, avgMinTemp)
            } else {
                throw Exception("No weather data available for the last 10 years")
            }
        } else {
            // If the year is not in the future, call the API for the given year
            val apiUrl = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Delhi,IN/$year-$date?key=$apiKey&include=days&elements=tempmax%2Ctempmin"
            val weatherInfo = fetchDataFromApi(apiUrl)
            weatherViewModel.saveWeatherData(Weather(date = date, year = year, maxTemp = weatherInfo.maxTemp, minTemp = weatherInfo.minTemp))
            weatherInfo
        }
    } catch (e: Exception) {
        throw Exception("Error fetching data: ${e.localizedMessage ?: e.toString()}")
    }
}

private suspend fun fetchDataFromApi(apiUrl: String): WeatherInfo {
    return withContext(Dispatchers.IO) {
        val urlConnection = URL(apiUrl).openConnection() as HttpsURLConnection
        urlConnection.requestMethod = "GET"
        urlConnection.doInput = true
        urlConnection.connect()

        val responseCode = urlConnection.responseCode
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            val response = urlConnection.inputStream.bufferedReader().use { it.readText() }
            val jsonResponse = JSONObject(response)
            val daysArray = jsonResponse.getJSONArray("days")
            val firstDay = daysArray.getJSONObject(0)
            val tempMax = firstDay.getDouble("tempmax")
            val tempMin = firstDay.getDouble("tempmin")
            WeatherInfo(tempMax, tempMin)
        } else {
            throw Exception("Error fetching data: HTTP response code $responseCode")
        }
    }
}
