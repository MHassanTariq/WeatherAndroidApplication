package com.example.weatherapp.RecyclerViewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weatherapp.Models.HomeModel
import com.example.weatherapp.Models.HourlyWeatherInfo
import com.example.weatherapp.Models.TodayWeatherInfo
import com.example.weatherapp.R
import kotlinx.android.synthetic.main.every_3hour_weather.view.*
import kotlinx.android.synthetic.main.todays_weather_info.view.*
import kotlinx.android.synthetic.main.weekly_layout_for_recycler_view.view.*

private const val TODAYS_WEATHER_INFO = 0
private const val EVERY_3HOUR_WEATHER = 1
private const val MAIN_ADAPTER_COUNT = 3

class MainAdapter(private val homeModel: HomeModel) : RecyclerView.Adapter<MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return MAIN_ADAPTER_COUNT
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        MainViewHolder.onBindViewHolder(holder, position, homeModel)
    }
}

class MainViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {
    private val myView = view

    companion object {

        fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            when (viewType) {
                TODAYS_WEATHER_INFO -> return createTodayWeatherInfo(parent)
                EVERY_3HOUR_WEATHER -> return createHourlyWeather(parent)
            }
            return createWeeklyWeather(parent)
        }

        private fun createWeeklyWeather(parent: ViewGroup): MainViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.weekly_layout_for_recycler_view, parent, false)
            return MainViewHolder(view)
        }

        private fun createHourlyWeather(parent: ViewGroup): MainViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.every_3hour_weather, parent, false)
            return MainViewHolder(view)
        }

        private fun createTodayWeatherInfo(parent: ViewGroup): MainViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.todays_weather_info, parent, false)
            return MainViewHolder(view)
        }

        fun onBindViewHolder(
            holder: MainViewHolder,
            position: Int,
            homeModel: HomeModel
        ) {
            when (position) {
                TODAYS_WEATHER_INFO -> homeModel.todayWeatherInfo?.let { currentWeatherInfo ->
                    bindTodayWeatherViews(holder, currentWeatherInfo)
                }
                EVERY_3HOUR_WEATHER -> homeModel.hourlyWeatherInfo?.let { listOfHours ->
                    bindTodaysHourlyWeatherViews(holder, listOfHours)
                }
                else -> homeModel.hourlyWeatherInfo?.let { listOfHours ->
                    bindPredictionWeatherViews(holder, listOfHours)
                }
            }
        }

        private fun bindPredictionWeatherViews(
            holder: MainViewHolder,
            listOfHours: List<HourlyWeatherInfo>
        ) {
            holder.myView.weekly_recycler_view.adapter = WeeklyAdapter(listOfHours)
        }

        private fun bindTodaysHourlyWeatherViews(
            holder: MainViewHolder,
            listOfHours: List<HourlyWeatherInfo>
        ) {
            holder.myView.pm12_temp.text = listOfHours.get(0).temperature.toString()
            holder.myView.pm3_temp.text = listOfHours.get(1).temperature.toString()
            holder.myView.pm6_temp.text = listOfHours.get(2).temperature.toString()
            holder.myView.pm9_temp.text = listOfHours.get(3).temperature.toString()
            holder.myView.am12_temp.text = listOfHours.get(4).temperature.toString()
        }

        private fun bindTodayWeatherViews(
            holder: MainViewHolder,
            todayWeatherInfo: TodayWeatherInfo
        ) {
            holder.myView.today_weather_description_text.text = todayWeatherInfo.desc
            holder.myView.today_date_text.text = todayWeatherInfo.todayDate
            holder.myView.today_location_text.text = todayWeatherInfo.location
            holder.myView.today_temperature_text.text = todayWeatherInfo.temperature.toString()
            val URL =
                "https://is4-ssl.mzstatic.com/image/thumb/Purple123/v4/9a/c1/5d/9ac15dd5-0614-52b5-6fe8-19df1b6dfad6/AppIcon-0-0-1x_U007emarketing-0-0-0-7-0-0-sRGB-0-0-0-GLES2_U002c0-512MB-85-220-0-0.png/246x0w.png"
            Glide.with(holder.myView.today_icon.context).load(URL).apply(
                RequestOptions()
                    .placeholder(R.drawable.very_sunny)
                    .error(R.drawable.error_img_loading)
            )
                .into(holder.myView.today_icon)
        }

    }
}