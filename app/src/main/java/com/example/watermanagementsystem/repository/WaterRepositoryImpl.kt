package com.example.watermanagementsystem.repository

import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.watermanagementsystem.api.apiInterface
import com.example.watermanagementsystem.worker.FetchWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WaterRepositoryImpl @Inject constructor(
    private val api: apiInterface,
    private val workManager: WorkManager
) : WaterRepository
{
    override suspend fun setUpPeriodicWork(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<FetchWorker>(repeatInterval = 1 , repeatIntervalTimeUnit = java.util.concurrent.TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(uniqueWorkName = "fire_notification_work" ,
            ExistingPeriodicWorkPolicy.UPDATE
            ,workRequest
        )
    }
    override suspend fun getWaterLevel(): Int {
        try {
            val response = withContext(Dispatchers.IO) {
                api.getWaterLevel()
            }
            return response
        }catch (e : Exception){
            Log.d("api" , e.message.toString())
        }
        return -99
    }

    override suspend fun getMoistureLevel(): Int {
        try{
            val response = withContext(Dispatchers.IO){
                api.getMoistureLevel()
            }
            return response
        }catch (e : Exception){
            Log.d("api" , e.message.toString())
        }
        return -99
    }

    override suspend fun getFireStatus(): String {
        try{
            val response = withContext(Dispatchers.IO){
                api.getFireStatus()
            }
            return response
            }catch (e : Exception){
            Log.d("api" , e.message.toString())
        }
        return "error"
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
}