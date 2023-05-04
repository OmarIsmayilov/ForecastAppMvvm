package com.example.weatherappmvvm.data.service

import com.example.weatherappmvvm.utils.Constants.BASE_URL

object ApiUtils {

    val instance by lazy { ApiService.getService().create(WeatherApi::class.java) }

}