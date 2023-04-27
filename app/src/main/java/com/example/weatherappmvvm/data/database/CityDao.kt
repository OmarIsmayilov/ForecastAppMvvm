package com.example.weatherappmvvm.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherappmvvm.data.model.City

@Dao
interface CityDao {

    @Query("SELECT * FROM cities ORDER BY city_id ASC")
    fun getAllCities() : LiveData<List<City>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCity(city:City)

    @Query("DELETE FROM cities")
    fun deleteAllCity()

    @Delete
    fun deleteCity(city:City)

}