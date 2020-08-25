package com.example.weatherapp.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
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
    private var mainAdapter: MainAdapter = MainAdapter(homeModel = HomeModel(null, null))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home, container, false
        )
        binding.locationName.visibility = View.GONE
        viewModel = getViewModelInstance()
        setObservers()
        setViewListeners()

        return binding.root
    }

    private fun setViewListeners() {
        binding.locationName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel?.refreshData(binding.locationName.text.toString())
            }
        }
    }

    private fun setObservers() {
        viewModel?.todayWeatherInfo?.observe(viewLifecycleOwner, Observer { todayWeatherInfo ->
            todayWeatherInfo?.let { dataToDisplay ->
                binding.homeNotification.visibility = View.GONE
                binding.locationName.visibility = View.VISIBLE
                mainAdapter.homeModel = dataToDisplay
                if (viewModel?.firstTimeDataSuccess == true)
                    binding.homeMainRecyclerView.adapter = mainAdapter
                else
                    mainAdapter.notifyDataSetChanged()
            }
        })
        viewModel?.locationNotFound?.observe(viewLifecycleOwner, Observer { isLocationNot ->
            if (isLocationNot) {
                Toast.makeText(activity, ERR_LOC_NOT_FOUND, Toast.LENGTH_LONG).show()
                binding.homeNotification.text = ERR_LOC_NOT_FOUND
                binding.homeNotification.setTextColor(Color.RED)
                viewModel?.locationNotFoundComplete()
            }
            else
                binding.homeNotification.setTextColor(Color.BLACK)
        })
        viewModel?.internetNotFound?.observe(viewLifecycleOwner, Observer { noNet ->
            if (noNet) {
                Toast.makeText(activity, ERR_NO_INTERNET, Toast.LENGTH_LONG).show()
                binding.homeNotification.text = ERR_NO_INTERNET
            }
        })
    }

    private fun getViewModelInstance(): HomeViewModel? = context?.let { applicationContext ->
        val viewModel: HomeViewModel by viewModels { HomeViewModelFactory(applicationContext) }
        return viewModel
    }

}