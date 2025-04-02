package com.example.watermanagementsystem.api

import com.example.watermanagementsystem.constant.API
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface apiInterface {

    @GET(API.WATER_LEVEL_ENDPOINT)
    suspend fun getWaterLevel(): Int

    @GET(API.MOISTURE_LEVEL_ENDPOINT)
    suspend fun getMoistureLevel(): Int

    @GET(API.FIRE_STATUS_ENDPOINT)
    suspend fun getFireStatus(): String

    @POST(API.EXTINGUISH_ENDPOINT+"/{toggle}")
    suspend fun extinguish(
        @Path("toggle") toggle: Int = 1
    )

}