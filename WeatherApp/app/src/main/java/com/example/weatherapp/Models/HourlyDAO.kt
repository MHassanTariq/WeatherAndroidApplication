package com.example.weatherapp.Models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HourlyDAO {
    @Insert
    fun insert(hourlyWeatherInfo: MutableList<HourlyWeatherInfo>)

    @Update
    fun update(hourlyWeatherInfo: HourlyWeatherInfo)

    @Query("SELECT * FROM hourly_weather_info")
    fun getHourlyData() : MutableList<HourlyWeatherInfo>

    @Query("DELETE FROM hourly_weather_info")
    fun clear()
}