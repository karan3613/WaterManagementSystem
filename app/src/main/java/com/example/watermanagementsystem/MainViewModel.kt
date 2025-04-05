package com.example.watermanagementsystem

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watermanagementsystem.repository.WaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WaterRepository
) : ViewModel(){
    val waterLevel = mutableFloatStateOf(0f)
    val moistureLevel = mutableFloatStateOf(0f)
    val fireStatus = mutableStateOf(false)

    init {
        setupPeriodicWork()
        fetchDetails()

    }

    private fun fetchDetails() {

            viewModelScope.launch {
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
            repository.extinguish()
        }
    }
    fun setupPeriodicWork(){
        viewModelScope.launch {
            repository.setUpPeriodicWork()
        }
    }
}