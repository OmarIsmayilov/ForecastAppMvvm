package com.example.weatherappmvvm.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity("cities")
data class City(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("city_id") val id: Int,
    @ColumnInfo("city_name") val name: String,
    @ColumnInfo("city_icon") val icon: String,
    @ColumnInfo("city_country") val country: String,
    @ColumnInfo("city_temp") val temp: String,
) : Parcelable
