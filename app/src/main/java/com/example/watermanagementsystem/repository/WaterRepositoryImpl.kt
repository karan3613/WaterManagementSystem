package com.example.watermanagementsystem.repository

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.watermanagementsystem.api.advancedMlApiInterface
import com.example.watermanagementsystem.model.DataModel
import com.example.watermanagementsystem.model.PredictedModel
import com.example.watermanagementsystem.model.PredictionModel
import com.example.watermanagementsystem.model.TapModel
import com.example.watermanagementsystem.api.apiInterface
import com.example.watermanagementsystem.api.mlApiInterface
import com.example.watermanagementsystem.constant.API
import com.example.watermanagementsystem.model.ChatbotRequest
import com.example.watermanagementsystem.model.ChatbotResponse
import com.example.watermanagementsystem.model.DiseaseResponse
import com.example.watermanagementsystem.worker.FetchWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class WaterRepositoryImpl @Inject constructor(
    private val mlApi: mlApiInterface,
    private val api: apiInterface,
    private val workManager: WorkManager ,
    private val advancedMlApi: advancedMlApiInterface ,
    @ApplicationContext private val context : Context
) : WaterRepository
{
    override suspend fun setUpPeriodicWork(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<FetchWorker>(repeatInterval = 15 , repeatIntervalTimeUnit = java.util.concurrent.TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(uniqueWorkName = "fire_notification_work" ,
            ExistingPeriodicWorkPolicy.UPDATE
            ,workRequest
        )
    }

    override suspend fun getData(): DataModel {
        try {
            val response = withContext(Dispatchers.IO) {
                api.getData()
            }
            Log.d("api" ,"the data is successfully fetched")
            Log.d("api" , response.toString())
            return response
        }catch (e : Exception){
            Log.d("api" , e.message.toString())
        }
        return DataModel(0f , 100f , false)
    }

    override suspend fun extinguish()  : TapModel{
        try{
            val response = withContext(Dispatchers.IO){
                api.extinguish()
            }
            Log.d("api" , "THE TAP FUNCTION IS WORKING")
            return response
        }catch (e : Exception){
            Log.d("api" , e.message.toString())
        }
        return TapModel(false)
    }

    override suspend fun prediction(predictionModel: PredictionModel): PredictedModel {
        try {
            val response = withContext(Dispatchers.IO){
                mlApi.predict(predictionModel)
            }
            return response
        }catch (e : Exception){
            Log.d("api" , e.message.toString())
        }
        val randomValue = (0..2).random()
        val list = listOf("Domestic Cleaning" , "Industrial Uses" , "Irrigation")
        return PredictedModel(prediction = list[randomValue])
    }

    override suspend fun getDiseaseResponse(
        imageFile: File ,
        language: String
    ) : DiseaseResponse {
        try {
            val response = withContext(Dispatchers.IO)
            {
                advancedMlApi.getDiseaseResponse(
                    image = MultipartBody.Part.createFormData(
                        API.TYPE_IMAGE,
                        imageFile.name,
                        imageFile.asRequestBody()
                    ) ,
                    lang = language
                )
            }
            return response
        }
        catch (e : Exception){
            Log.d("api" , e.message.toString())
        }
        return DiseaseResponse("THE API HAS ERROR")
    }

    override suspend fun getChatbotResponse(
        question: String,
        language: String
    ): ChatbotResponse {
       try {
           val response = withContext(Dispatchers.IO){
               advancedMlApi.getChatbotResponse(ChatbotRequest(
                   question = question ,
                   language = language
               ))
           }
           return response
       }catch (e : Exception){
           Log.d("api" , e.message.toString())
       }
       return ChatbotResponse("THE API HAS ERROR")
    }
}
