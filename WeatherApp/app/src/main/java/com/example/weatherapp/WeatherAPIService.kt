package com.example.weatherapp

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private const val BASE_URL = "https://api.openweathermap.org"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WeatherApiService {
    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") myID: String
    ): Response<TodayAPI>

    @GET("/data/2.5/forecast")
    suspend fun getForecastWeather(
        @Query("q") cityName: String,
        @Query("appid") myId: String
    ): Response<ForecastAPI>
}

object WeatherApiSingleton {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}

data class Weather(val main: String, val icon: String)
data class Main(val temp: Double, val temp_min: Double, val temp_max: Double)
data class TodayAPI(val main: Main, val weather: List<Weather>)

data class ForecastAPI(val list: List<HourlyData>)
data class HourlyData(val main: Main, val dt_txt: String, val weather: List<Weather>)
