package com.example.weatherapp.Models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodayWeatherInfo::class, HourlyWeatherInfo::class], version = 5, exportSchema = false)
abstract class WeatherDataBase : RoomDatabase() {

    abstract val todayDatabaseDAO: TodayDatabaseDAO
    abstract val hourlyDAO : HourlyDAO

    companion object {

        @Volatile
        private var INSTANCE: WeatherDataBase? = null

        fun getInstance(context: Context): WeatherDataBase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WeatherDataBase::class.java,
                        "weather_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}