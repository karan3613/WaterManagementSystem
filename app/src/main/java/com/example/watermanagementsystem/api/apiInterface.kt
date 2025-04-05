package com.example.watermanagementsystem.api

import com.example.watermanagementsystem.constant.API
import retrofit2.http.GET
import retrofit2.http.POST

interface apiInterface {
    @GET(API.DATA_ENDPOINT)
    suspend fun getData(): DataModel

    @POST(API.EXTINGUISH_ENDPOINT)
    suspend fun extinguish()
}