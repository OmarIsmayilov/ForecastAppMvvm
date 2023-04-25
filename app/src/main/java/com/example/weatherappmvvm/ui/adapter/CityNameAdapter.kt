package com.example.weatherappmvvm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.weatherappmvvm.utils.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappmvvm.data.model.SearchResponseItem
import com.example.weatherappmvvm.databinding.NameItemBinding

class CityNameAdapter(val listener : OnItemClickListener) : RecyclerView.Adapter<CityNameAdapter.NameHolder>(){
    private val nameList = arrayListOf<SearchResponseItem>()

    inner class NameHolder(val binding : NameItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(name: SearchResponseItem) {
            with(binding){
                tvNameResult.setText("${name.name}, ${name.country}")
                tvNameResult.setOnClickListener {
                    listener.onItemClick("${name.name}, ${name.country} ")
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameHolder {
        val view = NameItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NameHolder(view)
    }

    override fun getItemCount(): Int {
        return nameList.size
    }

    override fun onBindViewHolder(holder: NameHolder, position: Int) {
        val name = nameList[position]
        holder.bind(name)

    }

    fun updateList(newList : ArrayList<SearchResponseItem>){
        nameList.clear()
        nameList.addAll(newList)
        notifyDataSetChanged()
    }


}