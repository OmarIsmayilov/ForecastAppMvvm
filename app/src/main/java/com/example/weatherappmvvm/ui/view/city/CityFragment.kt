package com.example.weatherappmvvm.ui.view.city

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappmvvm.R
import com.example.weatherappmvvm.data.model.City
import com.example.weatherappmvvm.databinding.FragmentCityBinding
import com.example.weatherappmvvm.databinding.FragmentHomeBinding
import com.example.weatherappmvvm.ui.adapter.CityAdapter
import com.example.weatherappmvvm.utils.SwipeHelper


class CityFragment : Fragment() {
    private var _binding: FragmentCityBinding? = null
    private val binding get() = _binding as FragmentCityBinding
    private val cAdapter = CityAdapter()
    private lateinit var viewModel: CityViewModel
    private var cList = listOf<City>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observeEvents()

    }

    private fun observeEvents() {
        viewModel.readAllData.observe(viewLifecycleOwner, Observer {
            with(binding){
                if (!it.isNullOrEmpty()){
                    rvCity.adapter = cAdapter
                    cAdapter.updateList(it)
                    cList=it
                    Log.e("TAG", "observeEvents: $it", )
                }else{
                    Toast.makeText(requireContext(), "Look empty. Add new city ", Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    private fun setup() {
        with(binding) {
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.statusColorGray)
            viewModel = ViewModelProvider(this@CityFragment)[CityViewModel::class.java]
            ibBack.setOnClickListener { findNavController().navigate(CityFragmentDirections.actionCityFragmentToHomeFragment()) }
            ibAdd.setOnClickListener { findNavController().navigate(CityFragmentDirections.actionCityFragmentToAddFragment()) }

            val swipeHelper = object  : SwipeHelper(){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    when(direction){
                        ItemTouchHelper.LEFT -> {
                            cList.let{viewModel.deleteCity(it[viewHolder.adapterPosition])}
                        }
                    }
                }
            }
            val touchHelper = ItemTouchHelper(swipeHelper)
            touchHelper.attachToRecyclerView(rvCity)
        }

    }

}