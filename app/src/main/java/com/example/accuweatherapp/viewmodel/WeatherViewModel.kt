package com.example.accuweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accuweatherapp.data.DailyForecast
import com.example.accuweatherapp.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherViewModel : ViewModel() {

    // StateFlow to hold the list of daily forecasts
    private val _dailyForecasts = MutableStateFlow<List<DailyForecast>>(emptyList())
    val dailyForecasts: StateFlow<List<DailyForecast>> = _dailyForecasts

    // StateFlow to hold the headline text
    private val _headlineText = MutableStateFlow("Loading weather headline...")
    val headlineText: StateFlow<String> = _headlineText

    // StateFlow to manage loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // StateFlow to manage error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    private val API_KEY = "@YOUR_OWN_API_KEY" //API KEY (IF USING THIS APP CHANGE TO YOUR OWN API KEY)

    // Location Key for Puerto Rico (EXAMPLE CHANGE TO ANY LOCATION KEY OF YOUR CHOOSING)
    private val LOCATION_KEY = "1152461"

    init {
        fetchWeatherForecast()
    }

    // Function to fetch weather data from AccuWeather API
    fun fetchWeatherForecast() {
        _isLoading.value = true
        _errorMessage.value = null // Clear previous errors

        viewModelScope.launch {
            try {
                // Make the API call using Retrofit
                val response = RetrofitClient.instance.get5DayForecast(LOCATION_KEY, API_KEY)

                if (response.isSuccessful && response.body() != null) {
                    val weatherResponse = response.body()!!
                    _dailyForecasts.value = weatherResponse.dailyForecasts
                    _headlineText.value = weatherResponse.headline.text
                } else {
                    // Handle API error (e.g., invalid API key, rate limit exceeded)
                    _errorMessage.value = "Error: ${response.code()} - ${response.message()}"
                    if (response.code() == 401) {
                        _errorMessage.value = "Unauthorized: Please check your AccuWeather API Key."
                    } else if (response.code() == 400) {
                        _errorMessage.value = "Bad Request: Check location key or API parameters."
                    }
                }
            } catch (e: Exception) {
                // Handle network or parsing exceptions
                _errorMessage.value = "Network Error: ${e.localizedMessage ?: "Unknown error"}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Helper function to format date from ISO 8601 string
    fun formatDate(isoDateString: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val formatter = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
            formatter.format(parser.parse(isoDateString) ?: Date())
        } catch (e: Exception) {
            isoDateString // Return original if parsing fails
        }
    }
}
