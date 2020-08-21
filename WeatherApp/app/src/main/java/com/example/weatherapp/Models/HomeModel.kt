package com.example.weatherapp.Models

data class HomeModel(
    val todayWeatherInfo: TodayWeatherInfo?,
    val hourlyWeatherInfo: List<HourlyWeatherInfo>?
)