package com.example.watermanagementsystem.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.watermanagementsystem.MainViewModel
import com.example.watermanagementsystem.ui.theme.background
import com.example.watermanagementsystem.ui.theme.green
import com.example.watermanagementsystem.ui.theme.red

@Composable
fun CAMERA_RESULT_SCREEN(navController : NavHostController , viewModel : MainViewModel){

    LaunchedEffect(Unit) {
        viewModel.predictDisease()
    }
    Box(
        modifier = Modifier.fillMaxSize().background(background).padding(20.dp) ,
        contentAlignment = Alignment.Center
    ){
        if(viewModel.isLoading.value){
            CircularProgressIndicator(
                color = red,
                strokeCap = StrokeCap.Round ,
                strokeWidth = 3.dp
            )
        }else{
            Column(
                modifier = Modifier.fillMaxSize().background(background).verticalScroll(
                    rememberScrollState()
                ) ,
            ){
                Text(
                    text = viewModel.diseaseResponse.value.diseaseInfo ,
                    color = green ,
                    textAlign = TextAlign.Center ,
                    fontSize = 20.sp ,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}