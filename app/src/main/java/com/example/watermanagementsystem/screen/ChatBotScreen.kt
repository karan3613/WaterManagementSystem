package com.example.watermanagementsystem.screen

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watermanagementsystem.MainViewModel
import com.example.watermanagementsystem.ui.theme.background
import com.example.watermanagementsystem.ui.theme.green
import java.util.Locale

@Composable
fun ChatbotScreen(viewModel: MainViewModel){
    val context = LocalContext.current
    val isQuestionAsked = remember { mutableStateOf(false) }
    val activity = context as ComponentActivity
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            val matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val question = matches?.get(0).orEmpty()
            viewModel.getChatbotResponse(question)
            isQuestionAsked.value = true
        }
    }
    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(background)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(
                                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                            )
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                        }
                        launcher.launch(intent)
                    }
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(it)
        ){
            if(viewModel.isLoading.value){
                CircularProgressIndicator(
                    color = green ,
                    strokeWidth = 3.dp,
                    strokeCap = StrokeCap.Round
                )
            }else{
                if(!isQuestionAsked.value){
                    Text(
                        if(viewModel.isHindiSelected.value) "नीचे दिए गए बटन का उपयोग करके प्रश्न पूछें" else "ਹੇਠਾਂ ਦਿੱਤੇ ਬਟਨ ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਇੱਕ ਸਵਾਲ ਪੁੱਛੋ।" ,
                        color = green ,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp ,
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    viewModel.chatbotResponse.value.answer ,
                    color = green ,
                    fontWeight = FontWeight.Normal,
                    fontSize = 22.sp
                )
            }
        }
    }
}