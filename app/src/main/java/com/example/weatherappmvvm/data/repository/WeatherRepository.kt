package com.example.weatherappmvvm.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.weatherappmvvm.data.model.CurrentWeatherResponse
import com.example.weatherappmvvm.data.model.ForecastWeatherResponse
import com.example.weatherappmvvm.data.model.SearchResponse
import com.example.weatherappmvvm.data.model.SearchResponseItem
import com.example.weatherappmvvm.data.service.ApiUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherRepository {

    var _currentWeatherData = MutableLiveData<CurrentWeatherResponse>()
    var _forecastWeatherData = MutableLiveData<ForecastWeatherResponse>()
    var _searchData = MutableLiveData<SearchResponse>()
    var _searchDataDetail = MutableLiveData<CurrentWeatherResponse>()
    var _isLoading = MutableLiveData<Boolean>()
    val apiService = ApiUtils.instance
    var job: Job? = null

    init {
        _isLoading.value = true
    }
    suspend fun getCurrentWeatherData(query: String) {

        job = CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.getCurrentWeather(query)
                if (response.isSuccessful){
                    response.body().let {
                        _currentWeatherData.value = it
                        _isLoading.value = false
                    }

            }
        }
    }

    suspend fun getForecastWeatherData(query: String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.getForecastWeather(query)
                if (response.isSuccessful){
                    response.body().let {
                        _forecastWeatherData.value = it
                        _isLoading.value = false

                }
            }
        }
    }

    suspend fun getSearchResult(query: String){
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.getSearchResult(query)
                if (response.isSuccessful){
                    response.body().let {
                        _searchData.value = it


                }
            }
        }
    }

    suspend fun getSearchDetail(query: String){
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.getCurrentWeather(query)
                if (response.isSuccessful){
                    response.body().let {
                        _searchDataDetail.value = it
                    }

            }
        }
    }



    fun cancelJobs() {
        job?.cancel()
    }



}