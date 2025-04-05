package com.example.watermanagementsystem.worker

import android.Manifest
import android.R
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.watermanagementsystem.api.apiInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class FetchWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val api: apiInterface,
) : CoroutineWorker(context, workerParams){
    //Case-1 where there is fire -> notification
    //Case-2 when the user will will open the app
    //step -1 when the user opens the app our viewmodel fetches the status
    //step - 2 when the user has closed the app our workmanager fetches the status and sends a notification
    //so now the user opens the app to extinguish the fire then again the step-1 gets repeated so the user will
    //always see the current fire status
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        return try {
            val response = withContext(Dispatchers.IO) {
                api.getData()
            }
            if(response.fire_status){
                sendNotification()
            }
            Result.success()
        }catch(
            e: Exception
        ){
            Log.d("api" , e.message.toString())
            Result.failure()
        }
    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun sendNotification() {
     val title = "FIRE-DETECTED"
     val name = "Fire-Notification"
     val message = "Fire has broken out please quickly extinguish it "
     val desc = "Notifier the user when fire is detected in his farms"
     val notificationManager = NotificationManagerCompat.from(applicationContext)
        val channel = NotificationChannelCompat.Builder(CHANNEL_ID , NotificationManagerCompat.IMPORTANCE_HIGH)
         .setName(name)
         .setDescription(desc)
         .build()

        notificationManager.createNotificationChannel(channel)
        val notification = NotificationCompat.Builder(applicationContext , CHANNEL_ID)
         .setSmallIcon(R.drawable.ic_dialog_alert)
         .setContentTitle(title)
         .setContentText(message)
         .setPriority(NotificationCompat.PRIORITY_HIGH)
         .setAutoCancel(true)
         .build()

     notificationManager.notify(FIRE_NOTIFICATION_ID, notification)
    }
}

const val CHANNEL_ID = "fire_notification_channel_id"
const val FIRE_NOTIFICATION_ID = 1