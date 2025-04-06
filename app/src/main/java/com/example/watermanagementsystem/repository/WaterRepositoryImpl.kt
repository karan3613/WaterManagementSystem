package com.example.watermanagementsystem.repository

import android.util.Log
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.watermanagementsystem.api.DataModel
import com.example.watermanagementsystem.api.PredictedModel
import com.example.watermanagementsystem.api.PredictionModel
import com.example.watermanagementsystem.api.apiInterface
import com.example.watermanagementsystem.api.mlApiInterface
import com.example.watermanagementsystem.worker.FetchWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WaterRepositoryImpl @Inject constructor(
    private val mlApi: mlApiInterface,
    private val api: apiInterface,
    private val workManager: WorkManager
) : WaterRepository
{
    override suspend fun setUpPeriodicWork(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<FetchWorker>(repeatInterval = 2 , repeatIntervalTimeUnit = java.util.concurrent.TimeUnit.SECONDS)
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
        return DataModel(99f , 99f , false)
    }

    override suspend fun extinguish() {
        try{
            val response = withContext(Dispatchers.IO){
                api.extinguish()
            }
            return
        }catch (e : Exception){
            Log.d("api" , e.message.toString())
        }
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
        return PredictedModel(prediction =  "THE API HAS ERROR")
    }
}