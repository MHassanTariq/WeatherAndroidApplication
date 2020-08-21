package com.example.weatherapp.ViewModels

import android.content.Context
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.Models.Repository

class HomeViewModel(
    context: Context
) : BaseViewModel() {

    private val dataSource: Repository = Repository(context)
    val todayWeatherInfo =
        Transformations.map(dataSource._homeModel) { todayWeatherLiveData ->
            todayWeatherLiveData
        }

    init {
        refreshData()
    }

    private fun refreshData() {
        dataSource.initializeData()
    }

    override fun onCleared() {
        super.onCleared()
        dataSource.cancelJob()
    }
}

class HomeViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                context
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}