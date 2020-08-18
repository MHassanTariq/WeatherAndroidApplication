package com.example.weatherapp.Fragments

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.weatherapp.Models.TodayDatabaseDAO
import com.example.weatherapp.Models.TodayWeatherInfo
import com.example.weatherapp.Models.WeatherDataBase
import com.example.weatherapp.TodayAPI
import com.example.weatherapp.WeatherApiSingleton
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.time.LocalDate

class HomeViewModel(
    dataSource: WeatherDataBase, application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var todayWeatherInfo = MutableLiveData<TodayWeatherInfo>()
    val _todayWeatherInfo : LiveData<TodayWeatherInfo>
        get() = todayWeatherInfo

    private var todayDAO: TodayDatabaseDAO = dataSource.todayDatabaseDAO

    init {
        initializeData()
    }
//    private fun callAPI() {
//        WeatherApiSingleton.retrofitService.getCurrentWeather().enqueue(
//            object : Callback<TodayAPI> {
//                override fun onFailure(call: Call<TodayAPI>, t: Throwable) {
//                    Log.d("HomeViewModel", "onFailure: ${t.message}")
//                }
//
//                override fun onResponse(call: Call<TodayAPI>, response: Response<TodayAPI>) {
//                    Log.d("HomeViewModel", "onSuccess: ${response.body()}")
//                }
//
//            }
//        )
//    }
    private fun initializeData() {
        uiScope.launch {
            try {
                val api = WeatherApiSingleton.retrofitService.getCurrentWeather()
                val todaysWeather = api.await()
                val description = todaysWeather?.weather[0]?.main
                val temperature = (todaysWeather?.main?.temp - 32) * 5 / 9
                Log.d("HomeViewModel",todaysWeather.toString())

                val location = "Lahore"
                todayWeatherInfo.value = getTodayInfoFromDB()
                Log.d("HomeViewModel", todayWeatherInfo.value.toString())
            }
            catch (e: Exception) {
                Log.d("HomeViewModel", e.message.toString())
            }
        }
    }

    private suspend fun getTodayInfoFromDB(): TodayWeatherInfo? {
        return withContext(Dispatchers.IO) {
            var todayData = todayDAO.getTodayWeatherInfo()
            todayData
        }
    }

    private fun addData(dataEntry : TodayWeatherInfo) {
        uiScope.launch {
            setTodayWeatherInfo(dataEntry)
        }
    }

    private suspend fun setTodayWeatherInfo(testingDataEntry: TodayWeatherInfo) {
        withContext(Dispatchers.IO) {
            todayDAO.insert(testingDataEntry)
        }
    }
}

class HomeViewModelFactory(
    private val dataSource: WeatherDataBase,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}