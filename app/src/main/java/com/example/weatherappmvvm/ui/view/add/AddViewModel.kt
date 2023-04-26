package com.example.weatherappmvvm.ui.view.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappmvvm.data.database.CityDao
import com.example.weatherappmvvm.data.database.CityRoomDB
import com.example.weatherappmvvm.data.model.City
import com.example.weatherappmvvm.data.model.CurrentWeatherResponse
import com.example.weatherappmvvm.data.model.SearchResponse
import com.example.weatherappmvvm.data.model.SearchResponseItem
import com.example.weatherappmvvm.data.repository.CityRepository
import com.example.weatherappmvvm.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddViewModel(application: Application)  : AndroidViewModel(application) {
    private var weatherRepository : WeatherRepository
    private var cityRepository : CityRepository
    var searchData = MutableLiveData<SearchResponse>()
    var searchDataDetail = MutableLiveData<CurrentWeatherResponse>()



    init {
        val cityDao = CityRoomDB.getDatabase(application).cityDao()
        cityRepository = CityRepository(cityDao,application)
        weatherRepository = WeatherRepository()
        searchData = weatherRepository._searchData
        searchDataDetail = weatherRepository._searchDataDetail
    }

    fun search(query: String){
        viewModelScope.launch(Dispatchers.IO){
            weatherRepository.getSearchResult(query)
            searchData = weatherRepository._searchData
        }
    }

    fun getSearchDetail(query: String){
        viewModelScope.launch(Dispatchers.IO){
            weatherRepository.getSearchDetail(query)
            searchDataDetail = weatherRepository._searchDataDetail

        }
    }

    fun addCity(city:City){
        viewModelScope.launch(Dispatchers.IO){
            city.let {cityRepository.addCity(city)  }

        }
    }


}
