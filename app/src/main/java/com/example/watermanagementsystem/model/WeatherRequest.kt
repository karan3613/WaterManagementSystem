package com.example.watermanagementsystem.model

data class WeatherRequest(
    val language : String,
    val latitude : Double ,
    val longitude : Double ,
    val date : String
)

data class WeatherResponse(
    val weatherInfo : String
)
