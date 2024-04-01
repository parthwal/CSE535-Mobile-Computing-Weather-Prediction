// WeatherScreen.kt
package com.example.weather_app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel, context: MainActivity) {
    var date by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var weatherData by remember { mutableStateOf("Enter Date and Year") }
    var isError by remember { mutableStateOf(false) } // Track error state
    var isFetching by remember { mutableStateOf(false) } // Track fetching state
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
                onValueChange = {
                    date = it
                    isError = false // Reset error state when user types
                },
                label = { Text("Date (MM-DD)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isError // Apply error border color
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = year,
                onValueChange = {
                    year = it
                    isError = false // Reset error state when user types
                },
                label = { Text("Year (YYYY)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isError // Apply error border color
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Button(
                    onClick = {
                        if (date.isNotBlank() && year.isNotBlank() && !isFetching) {
                            scope.launch {
                                try {
                                    isFetching = true // Set fetching state to true
                                    val weatherInfo = fetchWeatherData(date, year, weatherViewModel)
                                    weatherData = "Max Temp: ${weatherInfo.maxTemp}F, Min Temp: ${weatherInfo.minTemp}F"
                                } catch (e: Exception) {
                                    weatherData = e.message ?: "Unknown error occurred"
                                    isError = true // Set error state to true when error occurs
                                } finally {
                                    isFetching = false // Set fetching state to false when done
                                }
                            }
                        } else if (isFetching) {
                            // If already fetching, return without performing any action
                            return@Button
                        } else {
                            weatherData = "Please enter a valid date and year."
                            isError = true // Set error state to true
                        }
                    },
                ) {
                    if (isFetching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp), // Set size to 32.dp
                            color = Color.White
                        ) // Show small white loading spinner while fetching
                    } else {
                        Text("Get Weather", Modifier.padding(vertical = 6.dp, horizontal = 8.dp), textAlign = TextAlign.Start)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                weatherViewModel.deleteAllWeatherData()
                                weatherData = "Weather data deleted successfully."
                            } catch (e: Exception) {
                                weatherData = e.message ?: "Unknown error occurred"
                            }
                        }
                    },
                    contentPadding = PaddingValues(0.dp),
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                try {
                                    weatherViewModel.deleteAllWeatherData()
                                    weatherData = "Weather data deleted successfully."
                                } catch (e: Exception) {
                                    weatherData = e.message ?: "Unknown error occurred"
                                }
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete All Weather Data",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display weather data only when not fetching and not in error state
            Text(
                text = weatherData,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = if (isFetching) Color.White else Color.Black // Change text color to white when fetching
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Button to check network connectivity
            Button(
                onClick = {
                    val isConnected = isNetworkAvailable(context)
                    weatherData = if (isConnected) "Network is available" else "No network connection"
                },
            ) {
                Text("Check Network Connectivity")
            }
        }
    }
}
