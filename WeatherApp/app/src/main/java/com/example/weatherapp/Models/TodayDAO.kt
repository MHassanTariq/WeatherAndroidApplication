package com.example.weatherapp.Models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodayDatabaseDAO{
    @Insert
    fun insert(todayWeatherInfo: TodayWeatherInfo)

    @Update
    fun update(todayWeatherInfo: TodayWeatherInfo)

    @Query("SELECT * FROM today_weather_info ORDER BY dayID DESC LIMIT 1")
    fun getTodayWeatherInfo() : TodayWeatherInfo?

    @Query("SELECT * FROM today_weather_info WHERE dayID = :key")
    fun get(key : Long) : LiveData<TodayWeatherInfo>

    @Query("DELETE FROM today_weather_info WHERE dayID = :key")
    fun delete(key : Long)

    @Query("DELETE FROM today_weather_info")
    fun deleteAll()
}