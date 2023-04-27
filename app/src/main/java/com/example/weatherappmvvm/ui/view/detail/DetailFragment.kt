package com.example.weatherappmvvm.ui.view.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weatherappmvvm.R
import com.example.weatherappmvvm.data.model.CurrentWeatherResponse
import com.example.weatherappmvvm.data.model.ForecastWeatherResponse
import com.example.weatherappmvvm.databinding.FragmentDetailBinding
import com.example.weatherappmvvm.databinding.FragmentHomeBinding
import com.example.weatherappmvvm.ui.adapter.DailyForecastAdapter
import com.example.weatherappmvvm.ui.adapter.HourlyForecastAdapter
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Locale

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding as FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    private val hourlyWeatherAdapter = HourlyForecastAdapter()
    private val dailyWeatherAdapter = DailyForecastAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
        observeEvents()

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

        }

    }

    private fun setData(it: CurrentWeatherResponse?) {
        with(binding) {
            if (it != null) {
                cWeather = it
                fragment = this@DetailFragment
                Glide.with(requireActivity()).load("https:${it.current.condition.icon}")
                    .into(ivWeather)
            }
        }

    }

    private fun setAppTheme(day: Int) {
        if (day == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.lySwipe.setBackgroundResource(R.drawable.main_bg_night)
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.statusColor)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.lySwipe.setBackgroundResource(R.drawable.main_bg_day)
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.statusColor)
        }
    }

    fun formatTime(time: String): CharSequence? {
        val parsedTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(time)
        return parsedTime?.let {
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(it).lowercase()
        }
    }

    private fun setAdapter(it: ForecastWeatherResponse) {
        binding.rvHourly.adapter = hourlyWeatherAdapter
        binding.rvDaily.adapter = dailyWeatherAdapter
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        hourlyWeatherAdapter.updateList(it.forecast.forecastday.get(0).hour.filter {
            val hourEpoch = it.timeEpoch.toLong()
            val hourDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(hourEpoch), ZoneId.systemDefault())
            val hour = hourDateTime.hour
            hour >= currentHour && hourDateTime.toLocalDate() == LocalDate.now()
        })
        dailyWeatherAdapter.updateList(it.forecast.forecastday)
    }


    private fun setup() {
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.statusColor)
        val city = args.city
        val name = "${city.name}, ${city.country}"
        viewModel.refresh(name)

        binding.lySwipe.setOnRefreshListener {
            viewModel.refresh(name)
            binding.lySwipe.isRefreshing = false
        }
    }

}