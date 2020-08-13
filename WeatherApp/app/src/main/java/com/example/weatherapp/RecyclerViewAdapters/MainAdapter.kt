package com.example.weatherapp.RecyclerViewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R


private val TODAYS_WEATHER_INFO = 0
private val EVERY_3HOUR_WEATHER = 1
private val WEEKLY_WEATHER_INFO = 2

class MainAdapter : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        when(position) {
            0 -> return TODAYS_WEATHER_INFO
            1 -> return EVERY_3HOUR_WEATHER
        }
        return WEEKLY_WEATHER_INFO
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }
}

class MyViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            when (viewType) {
                TODAYS_WEATHER_INFO -> return TodaysWeatherInfo(parent)
                EVERY_3HOUR_WEATHER -> return Every3HourWeather(parent)
            }
            return TodaysWeatherInfo(parent)
        }

        private fun Every3HourWeather(parent: ViewGroup): MyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.every_3hour_weather, parent, false)
            return MyViewHolder(view)
        }

        private fun TodaysWeatherInfo(parent: ViewGroup): MyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.todays_weather_info, parent, false)
            return MyViewHolder(view)
        }
    }

}
