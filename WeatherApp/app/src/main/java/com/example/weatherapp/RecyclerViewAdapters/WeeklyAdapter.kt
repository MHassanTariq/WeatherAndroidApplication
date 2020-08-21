package com.example.weatherapp.RecyclerViewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Models.HourlyWeatherInfo
import com.example.weatherapp.R
import kotlinx.android.synthetic.main.week_weather_info.view.*

class WeeklyAdapter(private val listOfHours: List<HourlyWeatherInfo>) :
    RecyclerView.Adapter<WeeklyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyViewHolder {
        return WeeklyViewHolder.onCreateViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: WeeklyViewHolder, position: Int) {
        WeeklyViewHolder.onBindViewHolder(holder, position, listOfHours)
    }

}

class WeeklyViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {
    private val myView = view

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
            var positionOnArray = position * 8 + 4
            var minMaxTemp =
                "${listOfHours.get(positionOnArray).temperature} / ${listOfHours
                    .get(positionOnArray).temperature}"
            holder.myView.lowest_highest_temp.text = minMaxTemp
        }


    }

}