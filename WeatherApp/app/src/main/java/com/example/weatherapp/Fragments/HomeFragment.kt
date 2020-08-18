package com.example.weatherapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.weatherapp.Models.Repository
import com.example.weatherapp.Models.WeatherDataBase
import com.example.weatherapp.R
import com.example.weatherapp.RecyclerViewAdapters.MainAdapter
import com.example.weatherapp.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment() {

    private lateinit var binding :  FragmentHomeBinding
    private lateinit var viewModel : HomeViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home, container,false)
        viewModel = getViewModelInstance()
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        viewModel._todayWeatherInfo.observe(viewLifecycleOwner, Observer {todayWeatherInfo ->
            todayWeatherInfo?.let {
                binding.homeMainRecyclerView?.adapter = MainAdapter(it)
            }
        })
    }

    private fun getViewModelInstance() : HomeViewModel {
        val application = requireNotNull(this.activity).application
        val dataSource = WeatherDataBase.getInstance(application)
        val viewModelFactory = HomeViewModelFactory(dataSource, application)
        return ViewModelProviders.of(
            this, viewModelFactory).get(HomeViewModel::class.java)
    }
}