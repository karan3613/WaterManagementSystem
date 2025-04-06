package com.example.watermanagementsystem.repository

import com.example.watermanagementsystem.api.DataModel
import com.example.watermanagementsystem.api.PredictedModel
import com.example.watermanagementsystem.api.PredictionModel

interface WaterRepository {
    suspend fun setUpPeriodicWork()
    suspend fun getData() : DataModel
    suspend fun extinguish()
    suspend fun prediction(predictionModel: PredictionModel) : PredictedModel
}