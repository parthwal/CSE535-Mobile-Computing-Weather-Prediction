# Weather Prediction App

This Android application provides weather predictions for a given date and location. It uses historical data and real-time API calls to fetch and display temperature information.

## Features

- Fetch weather data for a specific date and year
- Display maximum and minimum temperatures
- Store and retrieve weather data locally
- Predict future weather based on historical data
- Check network connectivity

## Technical Details

The app is built using Kotlin and leverages the following technologies and libraries:

- **Jetpack Compose**: For building the user interface
- **Room Database**: For local data storage and retrieval
- **Coroutines**: For asynchronous programming
- **ViewModel**: For managing UI-related data
- **Visual Crossing Weather API**: For fetching weather data

## Project Structure

The project consists of several key components:

1. **AppDatabase**: Manages the Room database instance
2. **Weather**: Data class representing weather information
3. **WeatherDao**: Data Access Object for database operations
4. **WeatherRepository**: Handles data operations and API calls
5. **WeatherScreen**: Composable function for the main UI
6. **WeatherViewModel**: Manages UI state and business logic

## How It Works

1. Users input a date (MM-DD) and year (YYYY) in the app.
2. The app first checks the local database for existing data.
3. If data is not found locally, it fetches from the Visual Crossing Weather API.
4. For future dates, it predicts weather based on historical data from the past 10 years.
5. Weather information is displayed, showing maximum and minimum temperatures.
6. All fetched data is stored locally for future use.

## Setup

1. Clone the repository
2. Open the project in Android Studio
3. Replace the `apiKey` in `WeatherRepository.kt` with your Visual Crossing Weather API key
4. Build and run the application on an Android device or emulator

## Network Connectivity

The app includes a feature to check network connectivity, ensuring users are aware of their connection status when attempting to fetch weather data.

## Data Management

Users can delete all stored weather data using the delete button in the UI, providing a way to clear the local cache if needed.

## Note

This app is designed for educational purposes and may require additional error handling and optimizations for production use.
