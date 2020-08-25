package com.example.weatherapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.CITY_NAME
import com.example.weatherapp.Models.Repository

class HomeViewModel(
    context: Context
) : BaseViewModel() {

    private val dataSource: Repository = Repository(context)
    val todayWeatherInfo =
        Transformations.map(dataSource._homeModel) { todayWeatherLiveData ->
            firstTimeDataSuccess = true
            todayWeatherLiveData
        }
    val internetNotFound = Transformations.map(dataSource._internetNotConnected) { isInternet ->
        isInternet
    }
    val locationNotFound = Transformations.map(dataSource._locationNotFound) { isLocation ->
        isLocation
    }
    var firstTimeDataSuccess : Boolean = false



    init {
        refreshData(CITY_NAME)
    }

    fun refreshData(cityName : String) {
        dataSource.initializeData(cityName)
    }

    override fun onCleared() {
        super.onCleared()
        dataSource.cancelJob()
    }

    fun locationNotFoundComplete() {
        dataSource.locationNotFoundComplete()
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