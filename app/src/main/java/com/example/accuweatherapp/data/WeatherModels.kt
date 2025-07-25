package com.example.accuweatherapp.data

import com.google.gson.annotations.SerializedName

// Main response object for the 5-day forecast
data class WeatherResponse(
    @SerializedName("Headline") val headline: Headline,
    @SerializedName("DailyForecasts") val dailyForecasts: List<DailyForecast>
)

data class Headline(
    @SerializedName("EffectiveDate") val effectiveDate: String,
    @SerializedName("EffectiveEpochDate") val effectiveEpochDate: Long,
    @SerializedName("Severity") val severity: Int,
    @SerializedName("Text") val text: String,
    @SerializedName("Category") val category: String,
    @SerializedName("EndDate") val endDate: String,
    @SerializedName("EndEpochDate") val endEpochDate: Long,
    @SerializedName("MobileLink") val mobileLink: String,
    @SerializedName("Link") val link: String
)

data class DailyForecast(
    @SerializedName("Date") val date: String,
    @SerializedName("EpochDate") val epochDate: Long,
    @SerializedName("Temperature") val temperature: Temperature,
    @SerializedName("Day") val day: DayNightForecast,
    @SerializedName("Night") val night: DayNightForecast,
    @SerializedName("Sources") val sources: List<String>,
    @SerializedName("MobileLink") val mobileLink: String,
    @SerializedName("Link") val link: String
)

data class Temperature(
    @SerializedName("Minimum") val minimum: ValueUnit,
    @SerializedName("Maximum") val maximum: ValueUnit
)

data class ValueUnit(
    @SerializedName("Value") val value: Double,
    @SerializedName("Unit") val unit: String,
    @SerializedName("UnitType") val unitType: Int
)

data class DayNightForecast(
    @SerializedName("Icon") val icon: Int,
    @SerializedName("IconPhrase") val iconPhrase: String,
    @SerializedName("HasPrecipitation") val hasPrecipitation: Boolean,
    @SerializedName("PrecipitationType") val precipitationType: String? = null,
    @SerializedName("PrecipitationIntensity") val precipitationIntensity: String? = null
)
