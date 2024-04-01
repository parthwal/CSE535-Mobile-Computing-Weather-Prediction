package com.example.weather_app.q1_backup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

data class WeatherInfo(val maxTemp: Double, val minTemp: Double)

class WeatherViewModel : ViewModel() {
    private val apiKey = "8EHXSYEBKG6YUUD8EQZL7E6DJ"

    suspend fun fetchWeatherData(date: String, year: String): WeatherInfo {
        return withContext(Dispatchers.IO) {
            try {
                val apiUrl = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Delhi,IN/${year}-${date}?key=$apiKey"
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
            } catch (e: Exception) {
                throw Exception("Error fetching data: ${e.localizedMessage ?: e.toString()}")
            }
        }
    }
}
