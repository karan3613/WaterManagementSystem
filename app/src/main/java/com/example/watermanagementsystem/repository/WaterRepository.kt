package com.example.watermanagementsystem.repository

import com.example.watermanagementsystem.model.ChatbotResponse
import com.example.watermanagementsystem.model.DataModel
import com.example.watermanagementsystem.model.DiseaseResponse
import com.example.watermanagementsystem.model.PredictedModel
import com.example.watermanagementsystem.model.PredictionModel
import com.example.watermanagementsystem.model.TapModel
import okhttp3.MultipartBody
import java.io.File

interface WaterRepository {
    suspend fun setUpPeriodicWork()
    suspend fun getData() : DataModel
    suspend fun extinguish() : TapModel
    suspend fun prediction(predictionModel: PredictionModel) : PredictedModel
    suspend fun getDiseaseResponse(imageFile : File , language : String) : DiseaseResponse
    suspend fun getChatbotResponse(question : String , language : String) : ChatbotResponse
}