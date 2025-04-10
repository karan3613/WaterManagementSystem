package com.example.watermanagementsystem.model

data class PredictionModel(
    val PH : Float = 7.38f ,
    val EC : Float  = 2245.60f,
    val ORP : Float = -10.10f ,
    val DO : Float  = 1.15f,
    val TDS : Float = 8572.40f ,
    val TSS : Float  = 4380.50f,
    val TS : Float  = 11510.50f,
    val TOTAL_N : Float  = 2155.30f,
    val NH4_N : Float = 98.50f,
    val TOTAL_P : Float  = 1025.30f,
    val PO4_P : Float = 612.30f,
    val COD : Float = 2185.30f,
    val BOD : Float = 2185.30f
)