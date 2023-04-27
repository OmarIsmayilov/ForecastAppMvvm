package com.example.weatherappmvvm.data.repository

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.weatherappmvvm.data.database.CityDao
import com.example.weatherappmvvm.data.model.City
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityRepository(private val cityDao: CityDao,private val application: Application) {
    var _readAllData : LiveData<List<City>> = cityDao.getAllCities()


    fun addCity(city: City) {
        CoroutineScope(Dispatchers.IO).launch {
            val cityList = _readAllData.value
            Log.e(TAG, "addCity: $cityList", )
            if (cityList != null && cityList.any { it.name == city.name }) {
                Toast.makeText(application, "City already exists", Toast.LENGTH_SHORT).show()
            } else {
                cityDao.addCity(city)
            }
        }
    }


    fun deleteAllCity(){
        CoroutineScope(Dispatchers.IO).launch{
            cityDao.deleteAllCity()
        }
    }

    fun deleteCity(city:City){
        CoroutineScope(Dispatchers.IO).launch {
            cityDao.deleteCity(city)
            Log.e("", "deleteCity: $city", )
        }
    }
}