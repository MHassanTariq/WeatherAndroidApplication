package com.example.weatherapp.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hourly_weather_info")
class HourlyWeatherInfo(
    @PrimaryKey(autoGenerate = true)
    val dayID: Long = 0L,

    @ColumnInfo(name = "weather_time")
    val time: String,

    @ColumnInfo(name = "weather_temperature")
    val temperature: Double,

    @ColumnInfo(name = "weather_icon")
    val iconID: String
)