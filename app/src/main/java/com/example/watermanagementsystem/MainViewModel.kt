package com.example.watermanagementsystem

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watermanagementsystem.api.PredictedModel
import com.example.watermanagementsystem.api.PredictionModel
import com.example.watermanagementsystem.repository.WaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WaterRepository ,
    private val context :  Context
) : ViewModel(){
    val waterLevel = mutableFloatStateOf(0f)
    val moistureLevel = mutableFloatStateOf(0f)
    val fireStatus = mutableStateOf(false)
    val tapStatus = mutableStateOf(false)
    val predictedModel = mutableStateOf(PredictedModel())
    val isLoading = mutableStateOf(false)

    init {
        setupPeriodicWork()
        fetchDetails()
    }

     fun predictMlModel(prediction : PredictionModel){
        viewModelScope.launch {
            isLoading.value = true
            val response = repository.prediction(prediction)
            predictedModel.value = response
            isLoading.value = false
        }
    }

    private fun fetchDetails() {
            viewModelScope.launch{
                while (true) {
                    val response = repository.getData()
                    waterLevel.floatValue = response.water_level
                    moistureLevel.floatValue = response.moisture_level
                    fireStatus.value = response.fire_status
                    delay(1000)
                }
            }
    }

     fun toggleExtinguish(){
        viewModelScope.launch {
            val response = repository.extinguish()
            tapStatus.value = response.tap_status
            Toast.makeText(
                context ,
                if(tapStatus.value) "TAP IS ON" else "TAP IS OFF" ,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun setupPeriodicWork(){
        viewModelScope.launch {
            repository.setUpPeriodicWork()
        }
    }
}