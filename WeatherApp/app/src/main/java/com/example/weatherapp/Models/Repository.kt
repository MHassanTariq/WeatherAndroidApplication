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

    private var isLocationFound = MutableLiveData<Boolean>()
    val _isLocationFound: LiveData<Boolean>
        get() = isLocationFound

    private var isNetworkConnected = MutableLiveData<Boolean>()
    val _isNetworkConnected: LiveData<Boolean>
        get() = isNetworkConnected

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

    fun initializeData(cityName: String) {
        uiScope.launch {
            try {
                getApiAndPrepareTodayWeatherData(cityName)
                getApiAndPrepareHourlyWeatherData(cityName)
                errorOrData()
            } catch (e: Exception) {
                isNetworkConnected.postValue(false)
                getDBData(e)
            }
        }
    }

    private fun errorOrData() {
        if (!todayWeatherInfo?.desc.isNullOrBlank() && hourlyWeatherInfo != null)
            homeModel.postValue(HomeModel(todayWeatherInfo, hourlyWeatherInfo))
        else
            isLocationFound.postValue(false)
    }

    private suspend fun getHourlyDataFromDB(): MutableList<HourlyWeatherInfo> {
        return withContext(Dispatchers.IO) {
            val data = hourlyDAO.getHourlyData()
            data
        }

    }

    private suspend fun getApiAndPrepareHourlyWeatherData(cityName: String) {
        val hourlyWeatherResponse =
            WeatherApiSingleton.retrofitService.getForecastWeather(cityName, WEATHER_API_KEY)
        hourlyWeatherInfo = getHourlyWeatherData(hourlyWeatherResponse)
        val hourlyInfo: List<HourlyWeatherInfo> = hourlyWeatherInfo ?: return
        clearHourlyDB()
        addHourlyWeather(hourlyInfo as MutableList<HourlyWeatherInfo>?)
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

    private fun getHourlyWeatherData(hourlyWeatherResponse: Response<ForecastAPI>)
            : List<HourlyWeatherInfo>? {
        val hourlyWeather = hourlyWeatherResponse.body()?.list ?: return null
        return hourlyWeather.map { hour ->
            HourlyWeatherInfo(
                time = hour.dt_txt,
                temperature = hour.main.temp,
                iconID = hour.weather[0].icon
            )
        }
    }

    private suspend fun getDBData(e: Exception) {
        Log.d("Repo", "Error: ${e.message}")
        todayWeatherInfo = getTodayInfoFromDB()
        hourlyWeatherInfo = getHourlyDataFromDB()
        homeModel.postValue(HomeModel(todayWeatherInfo, hourlyWeatherInfo))
    }

    fun afterLocationNotFound() {
        isLocationFound.postValue(true)
    }

    private suspend fun getApiAndPrepareTodayWeatherData(cityName: String) {
        val currentWeatherResponse = WeatherApiSingleton.retrofitService
            .getCurrentWeather(cityName, WEATHER_API_KEY)
        val todaysWeather: TodayWeatherInfo =
            getCurrentWeatherData(currentWeatherResponse)
        if (todaysWeather.desc.isNullOrBlank()) return
        todaysWeather.location = cityName
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
            desc = todaysWeather?.weather?.get(0)?.main,
            temperature = todaysWeather?.main?.temp?.toInt(),
            location = location,
            todayDate = currentDate,
            iconID = todaysWeather?.weather?.get(0)?.icon
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