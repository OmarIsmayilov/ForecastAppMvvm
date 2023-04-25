package com.example.weatherappmvvm.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherappmvvm.R
import com.example.weatherappmvvm.data.model.Forecastday
import com.example.weatherappmvvm.data.model.Hour
import com.example.weatherappmvvm.databinding.DailyWeatherItemBinding
import com.example.weatherappmvvm.databinding.HourlyWeatherItemBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DailyForecastAdapter : RecyclerView.Adapter<DailyForecastAdapter.dailyForecastHolder>() {

    private var forecastDataList = arrayListOf<Forecastday>()

    inner class dailyForecastHolder(val binding: DailyWeatherItemBinding) : RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SetTextI18n")
        fun bind(data: Forecastday) {
            with(binding){
                fWeather = data
                position=adapterPosition
                adapter =this@DailyForecastAdapter
                Glide.with(this.root).load("https:${data.day.condition.icon}").into(ivFWeather)

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): dailyForecastHolder {
        val view = DailyWeatherItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return dailyForecastHolder(view)
    }

    override fun getItemCount(): Int {
        return forecastDataList.size
    }

    override fun onBindViewHolder(holder: dailyForecastHolder, position: Int) {
        val data = forecastDataList[position]
        holder.bind(data)
    }

    fun updateList(newList : List<Forecastday>){
        forecastDataList.clear()
        forecastDataList.addAll(newList)
        notifyDataSetChanged()
    }

    fun formatTime(time:String) :CharSequence{
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormatter.parse(time)
        return SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
    }


}