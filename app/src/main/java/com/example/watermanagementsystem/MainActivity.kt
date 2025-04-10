package com.example.watermanagementsystem

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.watermanagementsystem.constant.APPROUTES
import com.example.watermanagementsystem.screen.CAMERA_RESULT_SCREEN
import com.example.watermanagementsystem.screen.CameraScreen
import com.example.watermanagementsystem.screen.MainScreen
import com.example.watermanagementsystem.ui.theme.WaterManagementSystemTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaterManagementSystemTheme {
                val navController = rememberNavController()
                val viewModel : MainViewModel = hiltViewModel()
                NavHost(navController = navController , startDestination = APPROUTES.MAIN_SCREEN){
                    composable(
                        route = APPROUTES.MAIN_SCREEN
                    ) {
                        MainScreen()
                    }
                    composable(
                        route = APPROUTES.CAMERA_SCREEN
                    ){
                        CameraScreen(navController , viewModel)
                    }
                    composable(
                        route = APPROUTES.CAMERA_RESULT_SCREEN
                    ) {
                        CAMERA_RESULT_SCREEN(navController , viewModel)
                    }
                    composable(
                    route = APPROUTES.CHATBOT_SCREEN
                    ) {

                    }
                }
            }
        }
    }
}


