package com.example.weatherappmvvm.data.model


import com.google.gson.annotations.SerializedName

data class ForecastWeatherResponse(
    @SerializedName("current")
    val current: Current,
    @SerializedName("forecast")
    val forecast: Forecast,
    @SerializedName("location")
    val location: Location
)