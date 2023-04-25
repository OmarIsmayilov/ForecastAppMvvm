package com.example.weatherappmvvm.ui.view.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappmvvm.data.model.SearchResponse
import com.example.weatherappmvvm.data.model.SearchResponseItem
import com.example.weatherappmvvm.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class AddViewModel  : ViewModel() {
    var searchData = MutableLiveData<SearchResponse>()
    var searchDataDetail = MutableLiveData<SearchResponseItem>()
    val weatherRepository = WeatherRepository()

    init {
        searchData = weatherRepository._searchData
        searchDataDetail = weatherRepository._searchDataDetail
    }
    fun search(query: String){
        viewModelScope.launch{
            weatherRepository.getSearchResult(query)
            searchData = weatherRepository._searchData
        }
    }

    fun getSearchDetail(query: String){
        viewModelScope.launch{
            weatherRepository.getSearchDetail(query)
            searchDataDetail = weatherRepository._searchDataDetail

        }
    }

}
