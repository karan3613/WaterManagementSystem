package com.example.watermanagementsystem.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watermanagementsystem.MainViewModel
import com.example.watermanagementsystem.R
import com.example.watermanagementsystem.model.PredictionModel
import com.example.watermanagementsystem.ui.theme.background
import com.example.watermanagementsystem.ui.theme.blue
import com.example.watermanagementsystem.ui.theme.green
import com.example.watermanagementsystem.ui.theme.lineColor
import com.example.watermanagementsystem.ui.theme.purple
import com.example.watermanagementsystem.ui.theme.red
import com.example.watermanagementsystem.ui.theme.secondaryBackground
import com.example.watermanagementsystem.worker.CHANNEL_ID
import com.example.watermanagementsystem.worker.FIRE_NOTIFICATION_ID

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current
    val permission = Manifest.permission.POST_NOTIFICATIONS

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
    val context = LocalContext.current
    RequestNotificationPermission()
    LaunchedEffect(viewModel.fireStatus.value){
        if(viewModel.fireStatus.value && PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context , Manifest.permission.POST_NOTIFICATIONS)){
            sendNotification(context)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .verticalScroll(rememberScrollState())
            .padding(15.dp) ,
        horizontalAlignment =  Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.Center
    ){
        TopBar(
            currentLanguage = viewModel.isHindiSelected.value ,
            onLanguageChange = {
                viewModel.isHindiSelected.value = !viewModel.isHindiSelected.value
            }
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth() ,
            color = lineColor ,
            thickness = 2.dp
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp))
        StatusComponent(color = blue , value = viewModel.waterLevel.floatValue.toString() , title = if(viewModel.isHindiSelected.value)"जल स्तर" else "ਪਾਣੀ ਦੇ ਪੱਧਰ")
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp))
        StatusComponent(color = purple, value = viewModel.moistureLevel.floatValue.toString() , title = if(viewModel.isHindiSelected.value)"नमी स्तर" else "ਨਮੀ ਦੇ ਪੱਧਰ")
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp))
        StatusComponent(color = red ,
            value = if(viewModel.fireStatus.value)
                if(viewModel.isHindiSelected.value) "आग लग गई" else "ਅੱਗ ਦਾ ਪਤਾ ਲੱਗਾ ਹੈ"
            else
                if (viewModel.isHindiSelected.value) "सुरक्षित" else "ਸੁਰੱਖਿਅਤ" ,
            title = if(viewModel.isHindiSelected.value) "आग की स्थिति" else "ਅੱਗ ਦੀ ਸਥਿਤੀ")
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp))
        ButtonComponent(onButtonClick = {
            viewModel.toggleExtinguish()
             }
            , color = green , text = if(viewModel.isHindiSelected.value) "नल खोलें" else "ਟੂਟੀ ਖੋਲ੍ਹੋ"
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp))
    }
}

@Composable
fun MlComponents(viewModel: MainViewModel) {
    var predictionModel by remember {
        mutableStateOf(PredictionModel())
    }
    InputComponent(
        value = predictionModel.PH.toString() ,
        onValueChange = {
           predictionModel =  predictionModel.copy(
               PH = it.toFloatOrNull()?:predictionModel.PH
           )
        } ,
        label = "PH"
    )
    InputComponent(
        value = predictionModel.EC.toString() ,
        onValueChange = {
            predictionModel =  predictionModel.copy(
                EC = it.toFloatOrNull()?:predictionModel.EC
            )
        } ,
        label = "EC"
    )
    InputComponent(
        value = predictionModel.ORP.toString() ,
        onValueChange = {
            predictionModel =  predictionModel.copy(
                ORP = it.toFloatOrNull()?:predictionModel.ORP
            )
        } ,
        label = "ORP"
    )
    InputComponent(
        value = predictionModel.DO.toString() ,
        onValueChange = {
            predictionModel =  predictionModel.copy(
                DO = it.toFloatOrNull()?:predictionModel.DO
            )
        } ,
        label = "DO"
    )
    InputComponent(
        value = predictionModel.TDS.toString() ,
        onValueChange = {
            predictionModel =  predictionModel.copy(TDS = it.toFloatOrNull()?:predictionModel.TDS)
        } ,
        label = "TDS"
    )
    InputComponent(
        value = predictionModel.TSS.toString() ,
        onValueChange = {
            predictionModel =  predictionModel.copy(TSS = it.toFloatOrNull()?:predictionModel.TSS)
        } ,
        label = "TSS"
    )
    InputComponent(
        value = predictionModel.TS.toString() ,
        onValueChange = {
            predictionModel =  predictionModel.copy(TS = it.toFloatOrNull()?:predictionModel.TS)
        } ,
        label = "TS"
    )
    InputComponent(
        value = predictionModel.TOTAL_N.toString() ,
        onValueChange = {
            predictionModel =  predictionModel.copy(TOTAL_N = it.toFloatOrNull()?:predictionModel.TOTAL_N)
        } ,
        label = "TOTAL_N"
    )
    InputComponent(
        value = predictionModel.NH4_N.toString() ,
        onValueChange = {
            predictionModel =  predictionModel.copy(NH4_N = it.toFloatOrNull()?:predictionModel.NH4_N)
        } ,
        label = "NH4_N"
    )
    InputComponent(
        value = predictionModel.TOTAL_P.toString(),
        onValueChange = {
            predictionModel =  predictionModel.copy(TOTAL_P = it.toFloatOrNull()?:predictionModel.TOTAL_P)
        } ,
        label = "TOTAL_P"
    )
    InputComponent(
        value = predictionModel.PO4_P.toString() ,
        onValueChange = {
            predictionModel =  predictionModel.copy(PO4_P = it.toFloatOrNull()?:predictionModel.PO4_P)
        } ,
        label = "PO4_P"
    )
    InputComponent(
        value = predictionModel.COD.toString() ,
        onValueChange = {
            predictionModel =  predictionModel.copy(COD = it.toFloatOrNull()?:predictionModel.COD)
        } ,
        label = "COD"
    )
    InputComponent(
        value = predictionModel.BOD.toString(),
        onValueChange = {
            predictionModel =  predictionModel.copy(BOD = it.toFloatOrNull()?:predictionModel.BOD)
        } ,
        label = "BOD"
    )
    ButtonComponent(
        isLoading = viewModel.isLoading.value,
        onButtonClick = {viewModel.predictMlModel(predictionModel)} ,
        color = green ,
        text = "Predict")
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(20.dp))
    Text(
        text = viewModel.predictedModel.value.prediction ,
        fontSize = 30.sp ,
        color = red ,
        fontWeight = FontWeight.SemiBold ,
        fontStyle = FontStyle.Normal ,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    )
}

@Composable
fun InputComponent(
    value : String ,
    onValueChange : (String) -> Unit ,
    label : String
){
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(top = 10.dp, bottom = 10.dp),
        value =value,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(text = label , fontSize = 15.sp , color = green , fontWeight = FontWeight.SemiBold)
        },
        placeholder = {
            Text(text = "ENTER VALUE" , fontSize = 15.sp , color = secondaryBackground , fontWeight = FontWeight.SemiBold)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(10.dp) ,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = green ,
            focusedBorderColor = green ,
            focusedTextColor = Color.White ,
            unfocusedBorderColor = green ,
            unfocusedTextColor = Color.White,
            focusedLabelColor = green
        )
    )
}

@Composable
fun ButtonComponent(isLoading : Boolean = false , onButtonClick: () -> Unit, color: Color, text: String) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = secondaryBackground ,
            disabledContentColor = color,
            disabledContainerColor = secondaryBackground,
            contentColor = color
        ),
        enabled = !isLoading ,
        onClick = onButtonClick,
    ){
        if(isLoading){
            CircularProgressIndicator(
                color = green ,
                strokeCap = StrokeCap.Round ,
                strokeWidth = 0.5.dp
            )
        }else{
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold ,
                fontSize = 20.sp ,
                color = color ,
                fontStyle = FontStyle.Normal
            )
        }
    }
}

@Composable
fun StatusComponent(color : Color = red , value : String = "FIRE-DETECTED" , title :String = "Fire Status") {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(secondaryBackground, shape = RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(start = 10.dp),
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
fun TopBar(
    currentLanguage : Boolean ,
    onLanguageChange : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp) ,
        verticalAlignment = Alignment.CenterVertically ,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Punjabi" ,
            color = Color.White ,
            fontSize = 20.sp ,
            fontWeight = FontWeight.SemiBold
        )
        Switch(
            checked = currentLanguage ,
            onCheckedChange = {
                onLanguageChange()
            }
        )
        Text(
            text = "Hindi" ,
            color = Color.White ,
            fontSize = 20.sp ,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
private fun sendNotification(context : Context) {
    Log.d("notification" , "The notification has been started")
    val title = "FIRE-DETECTED"
    val name = "Fire-Notification"
    val message = "Fire has broken out please quickly extinguish it "
    val desc = "Notifier the user when fire is detected in his farms"
    val notificationManager = NotificationManagerCompat.from(context)
    val channel = NotificationChannelCompat.Builder(CHANNEL_ID , NotificationManagerCompat.IMPORTANCE_HIGH)
        .setName(name)
        .setDescription(desc)
        .build()

    notificationManager.createNotificationChannel(channel)
    val notification = NotificationCompat.Builder(context , CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(FIRE_NOTIFICATION_ID, notification)
    Log.d("notification" , "The notification has been sent")
}