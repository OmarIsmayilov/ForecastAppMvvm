package com.example.weatherappmvvm.data.service

import com.example.weatherappmvvm.data.model.CurrentWeatherResponse
import com.example.weatherappmvvm.data.model.ForecastWeatherResponse
import com.example.weatherappmvvm.data.model.SearchResponse
import com.example.weatherappmvvm.utils.Constants.API_KEY
import com.example.weatherappmvvm.utils.Constants.HOST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherApi {


    @Headers(
        "x-rapidapi-key: $API_KEY",
        "x-rapidapi-host: $HOST"
    )
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") q : String,
        @Query("lang") lang : String="az"
        ) : Response<CurrentWeatherResponse>


    @Headers(
        "x-rapidapi-key: $API_KEY",
        "x-rapidapi-host: $HOST"
    )
    @GET("forecast.json")
    suspend fun getForecastWeather(
        @Query("q") q : String,
        @Query("days") days: Int = 5,
        @Query("lang") language: String = "az",
    ) : Response<ForecastWeatherResponse>


    @Headers(
        "x-rapidapi-key: $API_KEY",
        "x-rapidapi-host: $HOST"
    )
    @GET("search.json")
    suspend fun getSearchResult(
        @Query("q") q : String
    ): Response<SearchResponse>

}