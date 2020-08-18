package com.example.weatherapp.RecyclerViewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Models.TodayWeatherInfo
import com.example.weatherapp.R
import kotlinx.android.synthetic.main.todays_weather_info.view.*
import kotlinx.android.synthetic.main.weekly_layout_for_recycler_view.view.*

private val TODAYS_WEATHER_INFO = 0
private val EVERY_3HOUR_WEATHER = 1
private const val WEEKLY_WEATHER = 2
class MainAdapter(todayWeatherInfo: TodayWeatherInfo) : RecyclerView.Adapter<MainViewHolder>() {
    private val myTodayWeatherInfo = todayWeatherInfo

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        when(position) {
            0 -> return TODAYS_WEATHER_INFO
            1 -> return EVERY_3HOUR_WEATHER
        }
        return WEEKLY_WEATHER
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        MainViewHolder.onBindViewHolder(holder, position, myTodayWeatherInfo)
    }
}

class MainViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {
    private val myView = view

    companion object {

        fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MainViewHolder {
            when (viewType) {
                TODAYS_WEATHER_INFO -> return todaysWeatherInfo(parent)
                EVERY_3HOUR_WEATHER -> return Every3HourWeather(parent)
            }
            return weeklyWeather(parent)
        }

        fun onBindViewHolder(holder: MainViewHolder, position: Int, todayWeatherInfo: TodayWeatherInfo) {
            if(position == 0) {
                holder.myView.today_weather_description_text.text = todayWeatherInfo.desc
                holder.myView.today_date_text.text = todayWeatherInfo.todayDate
                holder.myView.today_location_text.text = todayWeatherInfo.location
                holder.myView.today_temperature_text.text = todayWeatherInfo.temperature.toString()
            }
        }

        private fun weeklyWeather(parent: ViewGroup): MainViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.weekly_layout_for_recycler_view, parent, false)
            view.weekly_recycler_view.adapter = WeeklyAdapter()
            return MainViewHolder(view)
        }

        private fun Every3HourWeather(parent: ViewGroup) : MainViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.every_3hour_weather, parent, false)
            return MainViewHolder(view)
        }

        private fun todaysWeatherInfo(parent: ViewGroup) : MainViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.todays_weather_info, parent, false)
            return MainViewHolder(view)
        }
    }
}