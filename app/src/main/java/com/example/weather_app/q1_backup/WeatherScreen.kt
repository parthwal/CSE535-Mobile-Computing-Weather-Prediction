package com.example.weather_app.q1_backup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel) {
    var date by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var weatherData by remember { mutableStateOf("Enter Date and Year") }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date (MM-DD)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Year (YYYY)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (date.isNotBlank() && year.isNotBlank()) {
                        scope.launch {
                            try {
                                val weatherInfo = weatherViewModel.fetchWeatherData(date, year)
                                weatherData = "Max Temp: ${weatherInfo.maxTemp}°C, Min Temp: ${weatherInfo.minTemp}°C"
                            } catch (e: Exception) {
                                weatherData = e.message ?: "Unknown error occurred"
                            }
                        }
                    } else {
                        weatherData = "Please enter a valid date and year."
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Get Weather")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = weatherData,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
