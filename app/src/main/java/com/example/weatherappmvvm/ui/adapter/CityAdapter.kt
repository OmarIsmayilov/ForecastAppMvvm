package com.example.weatherappmvvm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherappmvvm.data.model.City
import com.example.weatherappmvvm.data.model.CurrentWeatherResponse
import com.example.weatherappmvvm.databinding.CityItemBinding
import com.example.weatherappmvvm.ui.view.city.CityFragmentDirections

class CityAdapter : RecyclerView.Adapter<CityAdapter.cityHolder>()  {
    private val cityList = arrayListOf<City>()
    inner class cityHolder(val binding : CityItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: City) {
            with(binding){
                binding.data = data
                tvDegree.text = data.temp+"°"
                Glide.with(binding.root).load("https:${data.icon}").into(ivWeatherIcon)
                itemView.setOnClickListener {
                    Navigation.findNavController(it).navigate(CityFragmentDirections.actionCityFragmentToDetailFragment(data))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cityHolder {
        val view = CityItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return cityHolder(view)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun onBindViewHolder(holder: cityHolder, position: Int) {
        val data = cityList[position]
        holder.bind(data)
    }

    fun updateList(newList: List<City>){
        cityList.clear()
        cityList.addAll(newList)
        notifyDataSetChanged()
    }


}