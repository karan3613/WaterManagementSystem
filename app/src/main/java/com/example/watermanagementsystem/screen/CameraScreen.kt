package com.example.watermanagementsystem.screen

import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.watermanagementsystem.MainViewModel
import com.example.watermanagementsystem.constant.APPROUTES

@Composable
fun CameraScreen(navController : NavHostController , viewModel : MainViewModel){
    val applicationContext = LocalContext.current.applicationContext
    val controller  = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(
                LifecycleCameraController.IMAGE_CAPTURE
            )
            videoCaptureQualitySelector =   QualitySelector.from(Quality.SD)
        }
    }
    LaunchedEffect(viewModel.isClicked.value) {
        if(viewModel.isClicked.value){
            viewModel.isClicked.value = false
            navController.navigate(APPROUTES.CAMERA_RESULT_SCREEN)
        }
    }
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding) ,
            contentAlignment = Alignment.BottomCenter
        ){
            CameraPreview(controller , Modifier.fillMaxSize())
            Row(
                modifier = Modifier.fillMaxWidth().height(90.dp) ,
                verticalAlignment = Alignment.CenterVertically ,
                horizontalArrangement = Arrangement.Center
            ){
                IconButton(
                    modifier = Modifier.size(60.dp),
                    onClick = {
                        viewModel.takePicture(controller)
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White ,
                        containerColor = Color.White
                    )

                ){
                    Icon(
                        imageVector = Icons.Default.AddCircle ,
                        contentDescription = "RECORD BUTTON"
                    )
                }
            }
        }
    }
}