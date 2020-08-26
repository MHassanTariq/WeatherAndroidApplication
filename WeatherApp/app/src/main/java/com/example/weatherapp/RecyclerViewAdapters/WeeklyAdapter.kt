package com.example.weatherapp.RecyclerViewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Models.HourlyWeatherInfo
import com.example.weatherapp.*
import com.example.weatherapp.R
import kotlinx.android.synthetic.main.week_weather_info.view.*
import java.text.SimpleDateFormat
import java.util.*

class WeeklyAdapter(private val listOfHours: List<HourlyWeatherInfo>) :
    RecyclerView.Adapter<WeeklyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyViewHolder {
        return WeeklyViewHolder.onCreateViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return NUMBER_OF_DAYS
    }

    override fun onBindViewHolder(holder: WeeklyViewHolder, position: Int) {
        WeeklyViewHolder.onBindViewHolder(holder, position, listOfHours)
    }

}

class WeeklyViewHolder private constructor(private val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun onCreateViewHolder(parent: ViewGroup): WeeklyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.week_weather_info, parent, false)
            return WeeklyViewHolder(view)
        }

        fun onBindViewHolder(
            holder: WeeklyViewHolder,
            position: Int,
            listOfHours: List<HourlyWeatherInfo>
        ) {
            val positionOnArray = position * HOURLY_FORECASTS_PER_DAY + CURRENT_DAY_FORECASTS
            val hour = listOfHours[positionOnArray]
            holder.view.week_day_name.text = getDayOfWeek(hour.time)
            holder.view.lowest_highest_temp.text = hour.temperature.toString()
            holder.view.week_day_icon.loadImage(hour.iconID)
        }

        private fun getDayOfWeek(apiTime: String?): String {
            val stringToDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val d: Date = stringToDate.parse(apiTime)
            val dateToString = SimpleDateFormat("EEE", Locale.ENGLISH)
            return dateToString.format(d)
        }
    }
}