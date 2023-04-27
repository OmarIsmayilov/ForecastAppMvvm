package com.example.weatherappmvvm.ui.view.city

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherappmvvm.data.database.CityRoomDB
import com.example.weatherappmvvm.data.model.City
import com.example.weatherappmvvm.data.repository.CityRepository
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : AndroidViewModel(application) {
    var readAllData : LiveData<List<City>>
     val cityDao = CityRoomDB.getDatabase(application).cityDao()
     val repo = CityRepository(cityDao,application)

    init {
        readAllData = repo._readAllData
    }



    fun deleteAllCity(){
        viewModelScope.launch {
            repo.deleteAllCity()
        }
    }

    fun deleteCity(city:City){
        viewModelScope.launch{
            repo.deleteCity(city)
        }
    }


}