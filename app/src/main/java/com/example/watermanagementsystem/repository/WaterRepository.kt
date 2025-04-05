package com.example.watermanagementsystem.repository

import com.example.watermanagementsystem.api.DataModel

interface WaterRepository {
    suspend fun setUpPeriodicWork()
    suspend fun getData() : DataModel
    suspend fun extinguish()
}