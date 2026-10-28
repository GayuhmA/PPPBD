package com.example.intentapp

import androidx.annotation.DrawableRes

data class Disaster(
    val nameDisaster: String = "",
    val disasterType: String = "",
    @DrawableRes val imageResId: Int = R.drawable.ic_disaster_natural
)
