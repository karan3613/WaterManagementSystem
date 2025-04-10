package com.example.watermanagementsystem.screen

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraPreview(
    controller : LifecycleCameraController,
    modifier : Modifier = Modifier
){
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                // Preview is incorrectly scaled in Compose on some devices without this
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier
    )
}