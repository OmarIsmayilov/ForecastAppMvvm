package com.example.weatherappmvvm.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherappmvvm.R
import com.example.weatherappmvvm.data.model.Hour
import com.example.weatherappmvvm.databinding.HourlyWeatherItemBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HourlyForecastAdapter : RecyclerView.Adapter<HourlyForecastAdapter.forecastHolder>() {

    private var forecastDataList = arrayListOf<Hour>()

    inner class forecastHolder(val binding: HourlyWeatherItemBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(data: Hour) {
            with(binding){
                fWeather=data
                adapter = this@HourlyForecastAdapter
                position = adapterPosition
                Glide.with(itemView.context).load("https:${data.condition.icon}").into(ivForecast)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): forecastHolder {
        val view = HourlyWeatherItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return forecastHolder(view)
    }

    override fun getItemCount(): Int {
        return forecastDataList.size
    }

    override fun onBindViewHolder(holder: forecastHolder, position: Int) {
        val data = forecastDataList[position]
        holder.bind(data)
    }

    fun updateList(newList : List<Hour>){
        forecastDataList.clear()
        forecastDataList.addAll(newList)
        notifyDataSetChanged()
    }

     fun formatTime(time: String): CharSequence? {
        val parsedTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(time)
        return parsedTime?.let { SimpleDateFormat("HH:mm", Locale.getDefault()).format(parsedTime)}
    }


}