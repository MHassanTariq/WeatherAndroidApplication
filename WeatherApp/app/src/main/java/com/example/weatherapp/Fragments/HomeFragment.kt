package com.example.weatherapp.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.weatherapp.ERR_LOC_NOT_FOUND
import com.example.weatherapp.ERR_NO_INTERNET
import com.example.weatherapp.Models.HomeModel
import com.example.weatherapp.R
import com.example.weatherapp.RecyclerViewAdapters.MainAdapter
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.viewmodels.HomeViewModel
import com.example.weatherapp.viewmodels.HomeViewModelFactory


class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private var viewModel: HomeViewModel? = null
    private lateinit var mainAdapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home, container, false
        )
        viewModel = getViewModelInstance()
        initSettings()
        return binding.root
    }

    private fun initSettings() {
        searchViewVisibility(View.GONE)
        setObservers()
        setViewListeners()
    }

    private fun searchViewVisibility(viewVisibility: Int) {
        binding.locationName.visibility = viewVisibility
        binding.searchBtn.visibility = viewVisibility
    }

    private fun setViewListeners() {
        binding.searchBtn.setOnClickListener {
            viewModel?.refreshData(binding.locationName.text.toString())
        }
    }

    private fun setObservers() {
        val localViewModel: HomeViewModel = viewModel ?: return
        populateDataObserver(localViewModel)
        errorStatesObservers(localViewModel)
    }

    private fun errorStatesObservers(localViewModel: HomeViewModel) {
        locationErrObs(localViewModel)
        internetErrObs(localViewModel)
    }

    private fun internetErrObs(localViewModel: HomeViewModel) {
        localViewModel.internetNotFound.observe(viewLifecycleOwner, Observer { isNotNet ->
            if (isNotNet) {
                Toast.makeText(activity, ERR_NO_INTERNET, Toast.LENGTH_LONG).show()
                binding.homeNotification.text = ERR_NO_INTERNET
            }
        })
    }

    private fun locationErrObs(localViewModel: HomeViewModel) {
        localViewModel.locationNotFound.observe(viewLifecycleOwner, Observer { isLocationNot ->
            if (isLocationNot) {
                Toast.makeText(activity, ERR_LOC_NOT_FOUND, Toast.LENGTH_LONG).show()
                binding.homeNotification.text = ERR_LOC_NOT_FOUND
                binding.homeNotification.setTextColor(Color.RED)
                viewModel?.afterLocationNotFound()
            } else
                binding.homeNotification.setTextColor(Color.BLACK)
        })
    }

    private fun populateDataObserver(localViewModel: HomeViewModel) {
        localViewModel.todayWeatherInfo.observe(viewLifecycleOwner, Observer { todayWeatherInfo ->
            todayWeatherInfo?.let { dataToDisplay ->
                binding.homeNotification.visibility = View.GONE
                searchViewVisibility(View.VISIBLE)
                populateAdapter(localViewModel, dataToDisplay)
            }
        })
    }

    private fun populateAdapter(
        localViewModel: HomeViewModel,
        dataToDisplay: HomeModel
    ) {
        if (localViewModel.firstTimeDataSuccess) {
            mainAdapter = MainAdapter(homeModel = dataToDisplay)
            binding.homeMainRecyclerView.adapter = mainAdapter
        } else {
            mainAdapter.homeModel = dataToDisplay
            mainAdapter.notifyDataSetChanged()
        }
    }

    private fun getViewModelInstance(): HomeViewModel? = context?.let { applicationContext ->
        val viewModel: HomeViewModel by viewModels { HomeViewModelFactory(applicationContext) }
        return viewModel
    }

}