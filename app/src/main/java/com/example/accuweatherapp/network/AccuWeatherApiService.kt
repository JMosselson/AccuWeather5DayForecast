package com.example.accuweatherapp.network

import com.example.accuweatherapp.data.WeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Retrofit interface for AccuWeather API calls
interface AccuWeatherApiService {
    @GET("forecasts/v1/daily/5day/{locationKey}")
    suspend fun get5DayForecast(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String,
        @Query("details") details: Boolean = false,
        @Query("metric") metric: Boolean = true // Set to true for Celsius, false for Fahrenheit
    ): Response<WeatherResponse> // Use Response to get access to HTTP status codes
}

// Singleton object to provide Retrofit instance
object RetrofitClient {
    private const val BASE_URL = "https://dataservice.accuweather.com/"

    // Create a logging interceptor for debugging network requests
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY) // Logs request and response bodies
    }

    // Create an OkHttpClient with the logging interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Build the Retrofit instance
    val instance: AccuWeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Add the custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AccuWeatherApiService::class.java)
    }
}
