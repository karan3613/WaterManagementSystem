package com.example.watermanagementsystem

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watermanagementsystem.repository.WaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WaterRepository
) : ViewModel(){
    val waterLevel = mutableIntStateOf(0)
    val moistureLevel = mutableIntStateOf(0)
    val fireStatus = mutableStateOf("")

    init {
        setupPeriodicWork()
        fetchDetails()
    }

    private fun fetchDetails() {
        viewModelScope.launch {
            waterLevel.intValue = repository.getWaterLevel()
            moistureLevel.intValue = repository.getMoistureLevel()
            fireStatus.value = repository.getFireStatus()
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