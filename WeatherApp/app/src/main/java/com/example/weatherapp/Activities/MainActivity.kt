package com.example.weatherapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapp.R

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}