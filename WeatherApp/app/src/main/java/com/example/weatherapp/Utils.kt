package com.example.weatherapp

import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("displayImageFromInternet")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    var myobject = imgUrl?.let {
        val imgUri =
            imgUrl.toUri().buildUpon().scheme("https").build()
        Log.d("Utils", "imgUri : $imgUri")
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.very_sunny)
                .error(R.drawable.error_img_loading))
            .into(imgView)
    }
    Log.d("Utils", "$myobject")
}