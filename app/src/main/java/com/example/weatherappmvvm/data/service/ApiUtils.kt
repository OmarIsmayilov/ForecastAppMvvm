package com.example.weatherappmvvm.data.service

import com.example.weatherappmvvm.utils.Constants.BASE_URL

class ApiUtils {
    companion object{
        fun getApi() : WeatherApi {
            return ApiService.getService(BASE_URL).create(WeatherApi::class.java)
        }
    }
}