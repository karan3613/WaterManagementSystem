package com.example.watermanagementsystem.repository

interface WaterRepository {
    suspend fun setUpPeriodicWork()
    suspend fun getWaterLevel(): Int
    suspend fun getMoistureLevel(): Int
    suspend fun getFireStatus(): String
    suspend fun extinguish()
}