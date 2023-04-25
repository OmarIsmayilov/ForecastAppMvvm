package com.example.weatherappmvvm.ui.view.add

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.weatherappmvvm.R
import com.example.weatherappmvvm.data.model.SearchResponse
import com.example.weatherappmvvm.databinding.FragmentAddBinding
import com.example.weatherappmvvm.databinding.FragmentCityBinding
import com.example.weatherappmvvm.ui.adapter.CityNameAdapter
import com.example.weatherappmvvm.ui.view.home.HomeViewModel
import com.example.weatherappmvvm.utils.OnItemClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddFragment : BottomSheetDialogFragment(), OnItemClickListener {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding as FragmentAddBinding
    private val viewModel: AddViewModel by viewModels()
    private val nAdapter = CityNameAdapter(this)

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
            with(binding){
                if(it!=null){
                    etLat.setText(it.lat.toString())
                    etLon.setText(it.lon.toString())
                    etCountry.setText(it.country)
                    etCity.setText(it.name)
                    svCityName2.setQuery("${it.name}, ${it.country}",false)
                }
            }
        })
    }



    private fun setup() {
        with(binding) {
            svCityName2.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.search(query!!)
                    //Close soft keyboard
                    val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onItemClick(name: String) {
        with(binding) {
            viewModel.getSearchDetail(name)
            cvSearchResult2.visibility=View.INVISIBLE
        }
    }


}