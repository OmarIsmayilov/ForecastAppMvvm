package com.example.weatherappmvvm.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherappmvvm.data.model.City

@Database(entities = [City::class], version = 1, exportSchema = false)
abstract class CityRoomDB : RoomDatabase() {
    abstract fun cityDao(): CityDao

    companion object {
        @Volatile
        var INSTANCE: CityRoomDB? = null

        fun getDatabase(context: Context): CityRoomDB {
            var instance = INSTANCE
            if (instance != null) {
                return instance
            } else {
                synchronized(this) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CityRoomDB::class.java,
                        "cities"
                    ).build()
                    INSTANCE = instance
                    return instance!!
                }
            }
        }
    }

}