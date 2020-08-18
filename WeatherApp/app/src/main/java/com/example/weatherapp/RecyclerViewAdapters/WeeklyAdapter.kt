package com.example.weatherapp.RecyclerViewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import kotlinx.android.synthetic.main.fragment_home.view.*

class WeeklyAdapter : RecyclerView.Adapter<WeeklyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyViewHolder {
        return WeeklyViewHolder.onCreateViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return 7
    }

    override fun onBindViewHolder(holder: WeeklyViewHolder, position: Int) {

    }

}

class WeeklyViewHolder private constructor(view : View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun onCreateViewHolder(parent: ViewGroup): WeeklyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.week_weather_info, parent, false)
            return WeeklyViewHolder(view)
        }

    }

}