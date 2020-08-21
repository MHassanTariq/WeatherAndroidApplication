package com.example.weatherapp.Models

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.ForecastAPI
import com.example.weatherapp.TodayAPI
import com.example.weatherapp.WeatherApiSingleton
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Repository(context: Context) {
    //Constants
    private val defaultWeatherDescription = "Couldn't load"
    private val defaultWeatherTemperature: Int = -1
    private val weatherApiKey = "52a6bcff1b67538be432738eecb8ba32"
    private val cityName = "Lahore"
    private val datePattern = "dd/M/yy"


    //Coroutine
    private var repoJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + repoJob)

    //LiveData
    private var homeModel =  MutableLiveData<HomeModel>()
    val _homeModel: LiveData<HomeModel>
        get() = homeModel

    //Data
    private var todayWeatherInfo : TodayWeatherInfo? = null
    private var hourlyWeatherInfo : List<HourlyWeatherInfo>? = null

    //DAOs
    private var todayDAO: TodayDatabaseDAO
    private var hourlyDAO: HourlyDAO

    init {
        val dataSource = WeatherDataBase.getInstance(context)
        todayDAO = dataSource.todayDatabaseDAO
        hourlyDAO = dataSource.hourlyDAO
    }

    fun initializeData() {
        uiScope.launch {
            try {
                getApiAndPrepareTodayWeatherData()
                getApiAndPrepareHourlyWeatherData()
                homeModel.value = HomeModel(todayWeatherInfo, hourlyWeatherInfo)
            } catch (e: Exception) {
                getDBData(e)
            }
        }
    }

    private suspend fun getHourlyDataFromDB(): MutableList<HourlyWeatherInfo> {
        return withContext(Dispatchers.IO) {
            val data = hourlyDAO.getHourlyData()
            data
        }

    }

    private suspend fun getApiAndPrepareHourlyWeatherData() {
        val hourlyWeatherResponse =
            WeatherApiSingleton.retrofitService.getForecastWeather(cityName, weatherApiKey)
        val hourlyWeather: MutableList<HourlyWeatherInfo>? =
            getHourlyWeatherData(hourlyWeatherResponse)
        hourlyWeatherInfo = hourlyWeather
        clearHourlyDB()
        addHourlyWeather(hourlyWeather)
    }

    private suspend fun clearHourlyDB() {
        withContext(Dispatchers.IO) {
            hourlyDAO.clear()
        }
    }

    private suspend fun addHourlyWeather(hourlyWeather: MutableList<HourlyWeatherInfo>?) {
        withContext(Dispatchers.IO) {
            if (hourlyWeather != null) {
                hourlyDAO.insert(hourlyWeather)
            }
        }
    }

    private fun getHourlyWeatherData(hourlyWeatherResponse: Response<ForecastAPI>): MutableList<HourlyWeatherInfo>? {
        val hourlyWeather = hourlyWeatherResponse.body()
        return hourlyWeather?.list?.let { listOfHours ->
            var hourlyWeatherList = mutableListOf<HourlyWeatherInfo>()
            var temp: Double = 0.0
            var time: String
            for (hour in listOfHours) {
                temp = hour.main.temp
                time = hour.dt_txt
                hourlyWeatherList.add(HourlyWeatherInfo(time = time, temperature = temp))
            }
            hourlyWeatherList
        }
    }

    private suspend fun getDBData(e: Exception) {
        Log.d("Repo", "Error: ${e.message}")
        todayWeatherInfo = getTodayInfoFromDB()
        hourlyWeatherInfo = getHourlyDataFromDB()
    }

    private suspend fun getApiAndPrepareTodayWeatherData() {
        val currentWeatherResponse = WeatherApiSingleton.retrofitService
            .getCurrentWeather(cityName, weatherApiKey)
        val todaysWeather: TodayWeatherInfo =
            getCurrentWeatherData(currentWeatherResponse)
        todayWeatherInfo = todaysWeather
        updateData(todaysWeather)
    }

    private fun getCurrentWeatherData(currentWeatherResponse: Response<TodayAPI>)
            : TodayWeatherInfo {
        val todaysWeather = currentWeatherResponse.body()
        val sdf = SimpleDateFormat(datePattern)
        val currentDate = sdf.format(Date())
        val location = cityName

        return TodayWeatherInfo(
            dayID = 0L,
            desc = todaysWeather?.weather?.get(0)?.main ?: defaultWeatherDescription,
            temperature = todaysWeather?.main?.temp?.toInt()
                ?: defaultWeatherTemperature,
            location = location,
            todayDate = currentDate
        )
    }

    private suspend fun getTodayInfoFromDB(): TodayWeatherInfo? {
        return withContext(Dispatchers.IO) {
            val todayData = todayDAO.getTodayWeatherInfo()
            todayData
        }
    }

    private suspend fun updateData(testingDataEntry: TodayWeatherInfo) {
        withContext(Dispatchers.IO) {
            todayDAO.insert(testingDataEntry)
        }
    }

    fun cancelJob() {
        repoJob.cancel()
    }
}