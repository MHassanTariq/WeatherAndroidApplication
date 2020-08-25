package com.example.weatherapp.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "today_weather_info")
data class TodayWeatherInfo(

    @PrimaryKey(autoGenerate = true)
    val dayID : Long = 0L,

    @ColumnInfo(name = "weather_description")
    val desc : String?,

    @ColumnInfo(name = "weather_temperature")
    val temperature : Int?,

    @ColumnInfo(name = "weather_location")
    var location : String?,

    @ColumnInfo(name = "weather_date")
    val todayDate : String?,

    @ColumnInfo(name = "weather_icon")
    val iconID : String?
)