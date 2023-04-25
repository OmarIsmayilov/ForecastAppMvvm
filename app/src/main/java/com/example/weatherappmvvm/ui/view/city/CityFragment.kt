package com.example.weatherappmvvm.ui.view.city

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.weatherappmvvm.R
import com.example.weatherappmvvm.databinding.FragmentCityBinding
import com.example.weatherappmvvm.databinding.FragmentHomeBinding


class CityFragment : Fragment() {
    private var _binding: FragmentCityBinding? = null
    private val binding get() = _binding as FragmentCityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.statusColorGray)

        binding.ibBack.setOnClickListener { findNavController().navigate(CityFragmentDirections.actionCityFragmentToHomeFragment())}
        binding.ibAdd.setOnClickListener { findNavController().navigate(CityFragmentDirections.actionCityFragmentToAddFragment()) }
    }

}