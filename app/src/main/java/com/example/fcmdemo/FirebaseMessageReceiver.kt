package com.example.fcmdemo

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.time.LocalDateTime
import java.util.*
import kotlin.math.roundToInt

class FirebaseMessageReceiver : FirebaseMessagingService() {

    private var channel_id: String?=""
    private var intent: Intent?=null
    private var msg: String?=null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.notification != null) {
            showNotification(
                remoteMessage.notification!!.title,
                remoteMessage.notification!!.body
            )
        }
    }
    companion object {
        private const val TAG = "MyFirebaseMessagingService"

        fun refreshFcmToken() {
            FirebaseMessaging.getInstance().deleteToken().addOnSuccessListener {
                FirebaseMessaging.getInstance().token
            }
        }
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("firebaseMessageReceiver", "FCM token changed: $token")

    }

    // Method to get the custom Design for the display of
    // notification.
    @SuppressLint("ResourceType")
    private fun getCustomDesign(
        title: String?,
        message: String?
    ): RemoteViews {
        val remoteViews = RemoteViews(
            applicationContext.packageName,
            R.layout.notification
        )
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        Log.d("message is", message!!)

        remoteViews.setImageViewResource(R.id.icon, R.drawable.email)
        return remoteViews
    }

    fun showNotification(
        title: String?,
        message: String?
    ) {
        if(message!!.contains(" Group")) {
             intent = Intent(this, SubscribeUsersActivity::class.java)
             channel_id = "notification_channel"
            intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            var split = message!!.split(" Group");
            msg = split[0];
            intent!!.putExtra("msg", msg!!);
        }
        else{
            intent = Intent(this, MainActivity::class.java)
            channel_id = "notification_channel"
            intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
       val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        var builder = NotificationCompat.Builder(
            applicationContext,
            channel_id!!
        )
            .setSmallIcon(R.drawable.email)
            .setAutoCancel(true)
            .setVibrate(
                longArrayOf(
                    1000, 1000, 1000,
                    1000, 1000
                )
            )
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = if (Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.JELLY_BEAN
        ) {
            builder.setContent(
                getCustomDesign(title, message)
            )
        }
        else {
            builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.email)
        }
        val notificationManager = getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager
        if (Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.O
        ) {
            val notificationChannel = NotificationChannel(
                channel_id, "web_app",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(
                notificationChannel
            )
        }
        notificationManager.notify(Math.random().roundToInt(), builder.build())
    }
}