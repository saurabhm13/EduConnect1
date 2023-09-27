package com.example.educonnect

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.educonnect.ui.activity.MainActivity
import com.example.educonnect.ui.activity.SingleChatActivity
import com.example.educonnect.util.Constants.Companion.ID
import com.example.educonnect.util.Constants.Companion.IMAGE
import com.example.educonnect.util.Constants.Companion.NAME
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.checkerframework.checker.nullness.qual.NonNull
import kotlin.random.Random

private const val CHANNEL_ID = "my_channel"

class MyFirebaseMessagingService: FirebaseMessagingService() {

    private var userId: String? = null
    private var name: String? = null
    private var image: String? = null

    private lateinit var intoMain: Intent

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val data = remoteMessage.data
        if (data.isNotEmpty()) {
            userId = data["userId"]
            name = data["name"]
            image = data["image"]

            intoMain = Intent(this, MainActivity::class.java)
            intoMain.putExtra("openChatFragment", "true")
            intoMain.putExtra(ID, userId)
            intoMain.putExtra(NAME, name)
            intoMain.putExtra(IMAGE, image)

        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        intoMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intoMain,
            FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(remoteMessage.data["title"])
            .setContentText(remoteMessage.data["message"])
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notification)

    }

    override fun onNewToken(token: String) {
        Log.d("New Token", "Refreshed token: $token")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "My channel description"
        }
        notificationManager.createNotificationChannel(channel)
    }

}