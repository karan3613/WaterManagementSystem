package com.example.watermanagementsystem

import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watermanagementsystem.ui.theme.background
import com.example.watermanagementsystem.ui.theme.blue
import com.example.watermanagementsystem.ui.theme.green
import com.example.watermanagementsystem.ui.theme.lineColor
import com.example.watermanagementsystem.ui.theme.purple
import com.example.watermanagementsystem.ui.theme.red
import com.example.watermanagementsystem.ui.theme.secondaryBackground
import java.nio.file.WatchEvent

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current
    val permission = android.Manifest.permission.POST_NOTIFICATIONS

    // Only needed for API 33+
    val shouldShowPermissionDialog = remember {
        true
    }

    val permissionState = remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionState.value = isGranted
        if (isGranted) {
            Toast.makeText(context, "Permission granted!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (shouldShowPermissionDialog && !permissionState.value) {
            launcher.launch(permission)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()){
    RequestNotificationPermission()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(15.dp) ,
        horizontalAlignment =  Alignment.CenterHorizontally
    ) {
        TopBar()
        Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))
        StatusComponent(color = blue , value = viewModel.waterLevel.floatValue.toString() , title = "Water Level")
        Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))
        StatusComponent(color = purple, value =viewModel.moistureLevel.floatValue.toString() , title = "Moisture Level")
        Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))
        StatusComponent(color = red , value = if(viewModel.fireStatus.value) "FIRE-DETECTED" else "SAFE" , title = "Fire Status")
        Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))
        ButtonComponent(onButtonClick = { viewModel.toggleExtinguish() } , color = green , text = "Extinguish")
    }
}

@Composable
fun ButtonComponent(onButtonClick: () -> Unit, color: Color, text: String) {
    Button(
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = secondaryBackground , 
            contentColor = color
        ),
        onClick = onButtonClick,
    ){
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold ,
            fontSize = 20.sp ,
            color = color ,
            fontStyle = FontStyle.Normal
        )
    }
}

@Composable
fun StatusComponent(color : Color = red , value : String = "FIRE-DETECTED" , title :String = "Fire Status") {
    Column(
        modifier = Modifier.fillMaxWidth()
            .height(160.dp)
            .background(secondaryBackground , shape = RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(40.dp).padding(start = 10.dp),
            horizontalArrangement = Arrangement.Start ,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = title,
                color = Color.White ,
                fontWeight = FontWeight.SemiBold ,
                fontSize = 23.sp ,
                fontStyle = FontStyle.Normal
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth() ,
            thickness = 1.dp ,
            color = lineColor
        )
        Row(
            modifier = Modifier.fillMaxSize() ,
            horizontalArrangement = Arrangement.Center ,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = value ,
                color = color,
                fontWeight = FontWeight.Bold ,
                fontSize = 32.sp ,
                fontStyle = FontStyle.Normal
            )
        }
    }
}


@Composable
fun TopBar() {
    Column(
        modifier = Modifier.fillMaxWidth().height(40.dp) ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.Bottom
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth() ,
            color = lineColor ,
            thickness = 2.dp
        )
    }
}