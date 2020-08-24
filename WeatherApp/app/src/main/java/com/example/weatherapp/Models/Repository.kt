package com.example.weatherapp.Models

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.*
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Repository(context: Context) {
    //Coroutine
    private var repoJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + repoJob)

    //LiveData
    private var homeModel = MutableLiveData<HomeModel>()
    val _homeModel: LiveData<HomeModel>
        get() = homeModel

    //Data
    private var todayWeatherInfo: TodayWeatherInfo? = null
    private var hourlyWeatherInfo: List<HourlyWeatherInfo>? = null

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
                //implement error handling
                //will do after complete implementation of homepage
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
            WeatherApiSingleton.retrofitService.getForecastWeather(CITY_NAME, WEATHER_API_KEY)
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
            val hourlyWeatherList = mutableListOf<HourlyWeatherInfo>()
            for (hour in listOfHours) {
                hourlyWeatherList.add(
                    HourlyWeatherInfo(
                        time = hour.dt_txt,
                        temperature = hour.main.temp,
                        iconID = hour.weather[0].icon
                    )
                )
            }
            hourlyWeatherList
        }
    }

    private suspend fun getDBData(e: Exception) {
        Log.d("Repo", "Error: ${e.message}")
        todayWeatherInfo = getTodayInfoFromDB()
        hourlyWeatherInfo = getHourlyDataFromDB()
        homeModel.value = HomeModel(todayWeatherInfo, hourlyWeatherInfo)
    }

    private suspend fun getApiAndPrepareTodayWeatherData() {
        val currentWeatherResponse = WeatherApiSingleton.retrofitService
            .getCurrentWeather(CITY_NAME, WEATHER_API_KEY)
        val todaysWeather: TodayWeatherInfo =
            getCurrentWeatherData(currentWeatherResponse)
        todayWeatherInfo = todaysWeather
        updateData(todaysWeather)
    }

    private fun getCurrentWeatherData(currentWeatherResponse: Response<TodayAPI>)
            : TodayWeatherInfo {
        val todaysWeather = currentWeatherResponse.body()
        val sdf = SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH)
        val currentDate = sdf.format(Date())
        val location = CITY_NAME

        return TodayWeatherInfo(
            dayID = 0L,
            desc = todaysWeather?.weather?.get(0)?.main ?: DEFAULT_WEATHER_DESC,
            temperature = todaysWeather?.main?.temp?.toInt()
                ?: DEFAULT_WEATHER_TEMPERATURE,
            location = location,
            todayDate = currentDate,
            iconID = todaysWeather?.weather?.get(0)?.icon ?: DEFAULT_ICON_ID
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