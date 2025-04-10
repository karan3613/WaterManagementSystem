package com.example.watermanagementsystem.api

import com.example.watermanagementsystem.constant.API
import com.example.watermanagementsystem.model.PredictedModel
import com.example.watermanagementsystem.model.PredictionModel
import retrofit2.http.Body
import retrofit2.http.POST

interface mlApiInterface {
    @POST(API.PREDICTION_ENDPOINT)
    suspend fun predict(
        @Body predictionModel: PredictionModel
    ): PredictedModel
}