package com.example.weatherapp.Models

import android.content.Context
import kotlinx.coroutines.*

class Repository (context: Context) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var todayDAO: TodayDatabaseDAO
    private var database : WeatherDataBase
    private var todayWeatherInfo : TodayWeatherInfo? = null

    init {
        database = WeatherDataBase.getInstance(context)
        todayDAO = WeatherDataBase.getInstance(context).todayDatabaseDAO
    }

    private fun addData() {
        val testingDataEntry = TodayWeatherInfo(
            desc = "Cloudy",
            temperature = 16,
            location = "Lahore",
            todayDate = "29/06/2020"
        )
        uiScope.launch {
            setTodayWeatherInfo(testingDataEntry)
        }
    }

    private suspend fun setTodayWeatherInfo(testingDataEntry: TodayWeatherInfo) {
        withContext(Dispatchers.IO) {
            todayDAO.insert(testingDataEntry)
        }
    }

    private fun initializeData() {
        uiScope.launch {
            todayWeatherInfo = getTodayInfoFromDB()
        }
    }

    private suspend fun getTodayInfoFromDB(): TodayWeatherInfo? {
        return withContext(Dispatchers.IO) {
            var todayData = todayDAO.getTodayWeatherInfo()
            todayData
        }
    }

    fun getTodayData(): TodayWeatherInfo? {
        initializeData()
        return todayWeatherInfo

    }
}