package com.example.watermanagementsystem.api

import com.example.watermanagementsystem.constant.API
import com.example.watermanagementsystem.model.ChatbotRequest
import com.example.watermanagementsystem.model.ChatbotResponse
import com.example.watermanagementsystem.model.DiseaseResponse
import com.example.watermanagementsystem.model.PredictedModel
import com.example.watermanagementsystem.model.PredictionModel
import com.example.watermanagementsystem.model.WeatherRequest
import com.example.watermanagementsystem.model.WeatherResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface advancedMlApiInterface{

    @POST(API.ADVANCED_ML_CHATBOT_ENDPOINT)
    suspend fun getChatbotResponse(
        @Body chatbotRequest: ChatbotRequest
    ) : ChatbotResponse

    @Multipart
    @POST(API.ADVANCED_ML_DISEASE_ENDPOINT)
    suspend fun getDiseaseResponse(
        @Part image : MultipartBody.Part ,
        @Part("lang") lang : String
    ) : DiseaseResponse

    @POST(API.PREDICTION_ENDPOINT)
    suspend fun predict(
        @Body predictionModel: PredictionModel
    ): PredictedModel
}