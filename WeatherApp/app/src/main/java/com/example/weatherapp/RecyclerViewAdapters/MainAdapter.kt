package com.example.weatherapp.RecyclerViewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.example.weatherapp.*

enum class HoursIndexes(val index: Int) {
    TWELVE_PM(0),
    THREE_PM(1),
    SIX_PM(2),
    NINE_PM(3),
    TWELVE_AM(4)
}

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

class MainViewHolder private constructor(private val view: View) : RecyclerView.ViewHolder(view) {

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
            holder.view.weekly_recycler_view.adapter = WeeklyAdapter(listOfHours)
        }

        private fun bindTodaysHourlyWeatherViews(
            holder: MainViewHolder,
            listOfHours: List<HourlyWeatherInfo>
        ) {
            holder.view.pm12_temp.text =
                listOfHours[HoursIndexes.TWELVE_PM.index].temperature.toString()
            holder.view.pm3_temp.text =
                listOfHours[HoursIndexes.THREE_PM.index].temperature.toString()
            holder.view.pm6_temp.text =
                listOfHours[HoursIndexes.SIX_PM.index].temperature.toString()
            holder.view.pm9_temp.text =
                listOfHours[HoursIndexes.NINE_PM.index].temperature.toString()
            holder.view.am12_temp.text =
                listOfHours[HoursIndexes.TWELVE_AM.index].temperature.toString()
            holder.view.pm12_icon.loadImage(listOfHours[HoursIndexes.TWELVE_PM.index].iconID)
            holder.view.pm3_icon.loadImage(listOfHours[HoursIndexes.THREE_PM.index].iconID)
            holder.view.pm6_icon.loadImage(listOfHours[HoursIndexes.SIX_PM.index].iconID)
            holder.view.pm9_icon.loadImage(listOfHours[HoursIndexes.NINE_PM.index].iconID)
            holder.view.am12_icon.loadImage(listOfHours[HoursIndexes.TWELVE_AM.index].iconID)
        }

        private fun bindTodayWeatherViews(
            holder: MainViewHolder,
            todayWeatherInfo: TodayWeatherInfo
        ) {
            holder.view.today_weather_description_text.text = todayWeatherInfo.desc
            holder.view.today_date_text.text = todayWeatherInfo.todayDate
            holder.view.today_location_text.text = todayWeatherInfo.location
            holder.view.today_temperature_text.text = todayWeatherInfo.temperature.toString()
            holder.view.today_icon.loadImage(todayWeatherInfo.iconID)
        }
    }
}

fun ImageView.loadImage(iconID: String) {
    val baseUrlPrefix = "https://openweathermap.org/img/wn/"
    val baseUrlSuffix = "@2x.png"
    val url = "$baseUrlPrefix${iconID}$baseUrlSuffix"
    Glide.with(this.context).load(url).apply(
        RequestOptions()
            .placeholder(R.drawable.very_sunny)
            .error(R.drawable.error_img_loading)
    )
        .into(this)
}