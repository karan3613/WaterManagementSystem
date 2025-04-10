package com.example.watermanagementsystem.model

data class DataModel(
    val water_level: Float,
    val moisture_level: Float,
    val fire_status: Boolean
)

data class TapModel(
    val tap_status : Boolean
)
