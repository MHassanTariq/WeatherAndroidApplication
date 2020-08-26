package com.example.weatherapp.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.CITY_NAME
import com.example.weatherapp.Models.HomeModel
import com.example.weatherapp.Models.Repository

class HomeViewModel(
    context: Context
) : BaseViewModel() {

    private val dataSource: Repository = Repository(context)
    val todayWeatherInfo: LiveData<HomeModel> =
        Transformations.map(dataSource._homeModel) { todayWeatherLiveData ->
            firstTimeDataSuccess = true
            todayWeatherLiveData
        }
    val isInternetAvailable: LiveData<Boolean> =
        Transformations.map(dataSource._isNetworkConnected) { isInternet ->
            isInternet
        }
    val isLocationFound: LiveData<Boolean> =
        Transformations.map(dataSource._isLocationFound) { isLocation ->
            isLocation
        }
    var firstTimeDataSuccess: Boolean = false

    init {
        refreshData(CITY_NAME)
    }

    fun refreshData(cityName: String) {
        dataSource.initializeData(cityName)
    }

    override fun onCleared() {
        super.onCleared()
        dataSource.cancelJob()
    }

    fun afterLocationNotFound() {
        dataSource.afterLocationNotFound()
    }
}

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                context
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}