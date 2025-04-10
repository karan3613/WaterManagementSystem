package com.example.watermanagementsystem

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageSavedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watermanagementsystem.model.ChatbotResponse
import com.example.watermanagementsystem.model.DiseaseResponse
import com.example.watermanagementsystem.model.PredictedModel
import com.example.watermanagementsystem.model.PredictionModel
import com.example.watermanagementsystem.repository.WaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WaterRepository ,
    @ApplicationContext private val context :  Context
) : ViewModel(){


    val waterLevel = mutableFloatStateOf(0f)
    val moistureLevel = mutableFloatStateOf(0f)
    val fireStatus = mutableStateOf(false)
    val tapStatus = mutableStateOf(false)
    val predictedModel = mutableStateOf(PredictedModel())
    val isLoading = mutableStateOf(false)
    val isHindiSelected = mutableStateOf(true)
    val isClicked = mutableStateOf(false)
    private val imageFile = File(context.filesDir , "my_image.jepg")

    val chatbotResponse = mutableStateOf(ChatbotResponse())
    val diseaseResponse = mutableStateOf(DiseaseResponse())

    init {
        setupPeriodicWork()
        fetchDetails()
    }

    fun predictDisease(){
        viewModelScope.launch {
           isLoading.value = false
           val response = repository.getDiseaseResponse(
               imageFile= imageFile ,
               language =  if(isHindiSelected.value) "Hindi" else "Punjabi")
           diseaseResponse.value = response
            isLoading.value = false
        }
    }

    fun getChatbotResponse(question : String){
        viewModelScope.launch {
            isLoading.value = true
            val response = repository.getChatbotResponse(
                question = question ,
                language = if(isHindiSelected.value) "Hindi" else "Punjabi"
            )
            chatbotResponse.value = response
            isLoading.value = false
        }
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
                    moistureLevel.floatValue = 100f - response.moisture_level
                    fireStatus.value = response.fire_status
                    delay(1000)
                }
            }
    }

     fun toggleExtinguish(){
        viewModelScope.launch {
            val response = repository.extinguish()
            tapStatus.value = response.tap_status
//            Toast.makeText(
//                context ,
//                if(tapStatus.value) "TAP IS ON" else "TAP IS OFF" ,
//                Toast.LENGTH_SHORT
//            ).show()
        }
    }

    @SuppressLint
    fun takePicture(cameraController: LifecycleCameraController){
        cameraController.takePicture(
            ImageCapture.OutputFileOptions.Builder(imageFile).build(),
            ContextCompat.getMainExecutor(context) ,
            object : OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(context , "ImageCaptureSucceeded", Toast.LENGTH_LONG).show()
                    isClicked.value = true
                }
                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(context , "ImageCaptureFailed", Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    private fun setupPeriodicWork(){
        viewModelScope.launch {
            repository.setUpPeriodicWork()
        }
    }
}