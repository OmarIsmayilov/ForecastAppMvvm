package com.example.weatherappmvvm.ui.view.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappmvvm.data.model.CurrentWeatherResponse
import com.example.weatherappmvvm.data.model.ForecastWeatherResponse
import com.example.weatherappmvvm.data.model.SearchResponse
import com.example.weatherappmvvm.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    var currentWeatherData = MutableLiveData<CurrentWeatherResponse>()
    var forecastWeatherData = MutableLiveData<ForecastWeatherResponse>()
    var isLoading = MutableLiveData<Boolean>()
    val weatherRepository = WeatherRepository()

    init {
        currentWeatherData = weatherRepository._currentWeatherData
        forecastWeatherData = weatherRepository._forecastWeatherData
        isLoading = weatherRepository._isLoading
    }

    fun refresh(query:String){
        viewModelScope.launch {
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



    override fun onCleared() {
        super.onCleared()
        weatherRepository.cancelJobs()
    }


}