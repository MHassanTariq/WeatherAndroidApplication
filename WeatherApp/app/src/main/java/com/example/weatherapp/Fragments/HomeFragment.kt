package com.example.weatherapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.weatherapp.R
import com.example.weatherapp.RecyclerViewAdapters.MainAdapter
import com.example.weatherapp.ViewModels.HomeViewModel
import com.example.weatherapp.ViewModels.HomeViewModelFactory
import com.example.weatherapp.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private var viewModel: HomeViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home, container, false
        )
        viewModel = getViewModelInstance()
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        viewModel?.todayWeatherInfo?.observe(viewLifecycleOwner, Observer { todayWeatherInfo ->
            todayWeatherInfo?.let {
                binding.homeNotification.visibility = View.GONE
                binding.homeMainRecyclerView.adapter = MainAdapter(it)
            }
        })
    }

    private fun getViewModelInstance(): HomeViewModel? = context?.let { applicationContext ->
        //discuss what the following line does
        val viewModel: HomeViewModel by viewModels { HomeViewModelFactory(applicationContext) }
        return viewModel
    }

}