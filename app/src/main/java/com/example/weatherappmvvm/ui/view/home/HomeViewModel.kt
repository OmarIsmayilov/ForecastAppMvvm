package com.example.weatherappmvvm.ui.view.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappmvvm.data.model.CurrentWeatherResponse
import com.example.weatherappmvvm.data.model.ForecastWeatherResponse
import com.example.weatherappmvvm.data.model.SearchResponse
import com.example.weatherappmvvm.data.repository.WeatherRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var currentWeatherData = MutableLiveData<CurrentWeatherResponse>()
    var forecastWeatherData = MutableLiveData<ForecastWeatherResponse>()
    var searchData = MutableLiveData<SearchResponse>()

    var isLoading = MutableLiveData<Boolean>()
    val weatherRepository = WeatherRepository()
    var job: Job?=null

    init {
        currentWeatherData = weatherRepository._currentWeatherData
        forecastWeatherData = weatherRepository._forecastWeatherData
        searchData =weatherRepository._searchData
        isLoading = weatherRepository._isLoading
    }

    fun refresh(query:String){
        job?.cancel()
        job = viewModelScope.launch {
            try {
                weatherRepository.getCurrentWeatherData(query)
                weatherRepository.getForecastWeatherData(query)
                currentWeatherData = weatherRepository._currentWeatherData
                forecastWeatherData = weatherRepository._forecastWeatherData
            }catch (e:Exception){
                Log.e("Error", "refresh: ${e.message}", )
            }
        }

    }

    fun search(query: String){
        viewModelScope.launch{
            weatherRepository.getSearchResult(query)
            searchData = weatherRepository._searchData
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        weatherRepository.cancelJobs()
    }
}