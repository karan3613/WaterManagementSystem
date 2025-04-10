package com.example.watermanagementsystem.api

import com.example.watermanagementsystem.constant.API
import com.example.watermanagementsystem.model.DataModel
import com.example.watermanagementsystem.model.TapModel
import retrofit2.http.GET

interface apiInterface {
    @GET(API.DATA_ENDPOINT)
    suspend fun getData(): DataModel

    @GET(API.EXTINGUISH_ENDPOINT)
    suspend fun extinguish() : TapModel
}