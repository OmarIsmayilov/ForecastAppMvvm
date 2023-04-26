package com.example.weatherappmvvm.ui.view.add

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappmvvm.R
import com.example.weatherappmvvm.data.model.City
import com.example.weatherappmvvm.data.model.CurrentWeatherResponse
import com.example.weatherappmvvm.databinding.FragmentAddBinding
import com.example.weatherappmvvm.ui.adapter.CityNameAdapter
import com.example.weatherappmvvm.utils.OnItemClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddFragment : BottomSheetDialogFragment(), OnItemClickListener {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding as FragmentAddBinding
    private lateinit var viewModel: AddViewModel
    private val nAdapter = CityNameAdapter(this)
    private lateinit var weatherData: CurrentWeatherResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observeEvents()
    }

    private fun observeEvents() {
        viewModel.searchData.observe(viewLifecycleOwner, Observer {
            with(binding) {
                if (!it.isNullOrEmpty()) {
                    cvSearchResult2.visibility = View.VISIBLE
                    rvName2.adapter = nAdapter
                    nAdapter.updateList(it)
                } else {
                    cvSearchResult2.visibility = View.INVISIBLE
                }
            }
        })
        viewModel.searchDataDetail.observe(viewLifecycleOwner, Observer {
            with(binding) {
                if (it != null) {
                    etLat.setText(it.location.lat.toString())
                    etLon.setText(it.location.lon.toString())
                    etCountry.setText(it.location.country)
                    etCity.setText(it.location.name)
                    svCityName2.setQuery("${it.location.name}, ${it.location.country}", false)
                    weatherData = it
                }
            }
        })
    }


    private fun setup() {
        with(binding) {
            viewModel = ViewModelProvider(this@AddFragment)[AddViewModel::class.java]
            svCityName2.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.search(query!!)
                    //Close soft keyboard
                    val inputMethodManager =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    if (inputMethodManager.isAcceptingText) {
                        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

            ibCreate.setOnClickListener {
                addToDb()
            }
        }
    }

    private fun addToDb() {
        val name = binding.etCity.text.toString()
        val country = binding.etCountry.text.toString()
        weatherData.let {
            val city = City(
                0,
                weatherData.location.name,
                weatherData.current.condition.icon,
                weatherData.location.country,
                weatherData.current.tempC.toInt().toString()
            )
            if (name.isNotEmpty() && country.isNotEmpty()) {
                city.let { viewModel.addCity(city) }
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Choose city and country", Toast.LENGTH_SHORT).show()
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onItemClick(name: String) {
        with(binding) {
            viewModel.getSearchDetail(name)
            cvSearchResult2.visibility = View.INVISIBLE
        }
    }


}