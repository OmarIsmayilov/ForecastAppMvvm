package com.example.weatherappmvvm.ui.view.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.weatherappmvvm.R
import com.example.weatherappmvvm.data.model.CurrentWeatherResponse
import com.example.weatherappmvvm.data.model.ForecastWeatherResponse
import com.example.weatherappmvvm.databinding.FragmentHomeBinding
import com.example.weatherappmvvm.ui.adapter.CityNameAdapter
import com.example.weatherappmvvm.ui.adapter.DailyForecastAdapter
import com.example.weatherappmvvm.ui.adapter.HourlyForecastAdapter
import com.example.weatherappmvvm.utils.OnItemClickListener
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment(),OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private var query: String? = null
    private val hAdapter = HourlyForecastAdapter()
    private val dAdapter = DailyForecastAdapter()
    private val nAdapter = CityNameAdapter(this)
    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observeEvents()
    }

    private fun setup() {
        with(binding) {
            GET = requireContext().getSharedPreferences("query", Context.MODE_PRIVATE)
            SET = GET.edit()
            query = GET.getString("query", null)
            query?.let { refresh(it) }
            val theme = GET.getBoolean("isDay", true)

            lySwipe.setBackgroundResource(
                if (theme) {
                    R.drawable.main_bg_day
                } else {
                    R.drawable.main_bg_night
                }
            )

            ibAddFragment.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCityFragment()) }

            lySwipe.setOnRefreshListener {
                query?.let { refresh(it) }
                lySwipe.isRefreshing = false
            }

            svCityName.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.search(query!!)
                    Log.e("TAG", "onQueryTextSubmit: $query")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

        }
    }


    private fun observeEvents() {
        with(binding) {
            viewModel.currentWeatherData.observe(
                viewLifecycleOwner,
                Observer { it.let { setData(it) } })

            viewModel.forecastWeatherData.observe(viewLifecycleOwner, Observer {
                it.let {
                    fWeather = it
                    setAdapter(it)
                    setAppTheme(it.current.isDay)
                }
            })

            viewModel.isLoading.observe(viewLifecycleOwner, Observer {
                if (it) {
                    lyMain.visibility = View.INVISIBLE
                    lyShimmer.visibility = View.VISIBLE
                } else {
                    lyShimmer.visibility = View.INVISIBLE
                    lyMain.visibility = View.VISIBLE

                }

            })

            viewModel.searchData.observe(viewLifecycleOwner, Observer {
                if (!it.isNullOrEmpty()) {
                    cvSearchResult.visibility = View.VISIBLE
                    rvName.adapter = nAdapter
                    nAdapter.updateList(it)
                } else {
                    cvSearchResult.visibility = View.INVISIBLE
                }
            })
        }

    }

    private fun setAdapter(it: ForecastWeatherResponse) {
        binding.rvHourly.adapter = hAdapter
        binding.rvDaily.adapter = dAdapter
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        hAdapter.updateList(it.forecast.forecastday.get(0).hour.filter {
            val hourEpoch = it.timeEpoch.toLong()
            val hourDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(hourEpoch), ZoneId.systemDefault())
            val hour = hourDateTime.hour
            hour >= currentHour && hourDateTime.toLocalDate() == LocalDate.now()
        })
        dAdapter.updateList(it.forecast.forecastday)
    }

    private fun setData(it: CurrentWeatherResponse?) {
        with(binding) {
            if (it != null) {
                cWeather = it
                fragment = this@HomeFragment
                Glide.with(requireActivity()).load("https:${it.current.condition.icon}")
                    .into(ivWeather)
            }
        }
    }

    private fun refresh(query: String) {
        viewModel.refresh(query)
    }

    private fun setAppTheme(day: Int) {
        if (day == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.lySwipe.setBackgroundResource(R.drawable.main_bg_night)
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.statusColor)
            SET.putBoolean("isDay", false).apply()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.lySwipe.setBackgroundResource(R.drawable.main_bg_day)
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.statusColor)
            SET.putBoolean("isDay", true).apply()
        }
    }

    fun formatTime(time: String): CharSequence? {
        val parsedTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(time)
        return parsedTime?.let {
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(it).lowercase()
        }
    }

    override fun onItemClick(name: String) {
        viewModel.refresh(name)
        binding.cvSearchResult.visibility = View.INVISIBLE
        binding.svCityName.setQuery(name,false)
        SET.putString("query",name).apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.job?.cancel()
    }

}