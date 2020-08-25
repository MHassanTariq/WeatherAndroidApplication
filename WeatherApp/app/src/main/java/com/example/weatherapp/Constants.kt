package com.example.weatherapp

//Repository Constants
const val WEATHER_API_KEY = "52a6bcff1b67538be432738eecb8ba32"
const val CITY_NAME = "Lahore"
const val DATE_PATTERN = "EEE dd-MM-yy"

//Main Adapter Constants
const val TODAYS_WEATHER_INFO = 0
const val THREE_HOUR_WEATHER = 1
const val MAIN_ADAPTER_COUNT = 3
const val BASE_URL_PREFIX = "https://openweathermap.org/img/wn/"
const val BASE_URL_SUFFIX = "@2x.png"

//Weekly Adapter Constants
const val NUMBER_OF_DAYS = 5
const val HOURLY_FORECASTS_PER_DAY = 8
const val CURRENT_DAY_FORECASTS = 5

//Error Messages (HomeFragment)
const val ERR_LOC_NOT_FOUND = "Sorry, we currently don't provide services in that area."
const val ERR_NO_INTERNET = "Please connect to internet and try again."
