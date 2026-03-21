package com.foodsaver.app.coreFcm.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.foodsaver.app.core.module.core.fcm.R
import com.foodsaver.app.utils.HttpConstants
import com.foodsaver.app.utils.saveNetworkCall
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class FcmService : FirebaseMessagingService(), KoinComponent {

    private val httpClient: HttpClient by inject()
    val serviceJob = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        println("FCM message data is ${message.data}")
        println("FCM message notification body is ${message.notification?.body}")
        if (message.data.isNotEmpty()) {

            message.notification?.let {
                it.body?.let { body ->
                    val productId = message.data["product_id"]
                    sendNotification(message = body, productId)
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("FCM New token is $token")

        serviceJob.launch {
            saveNetworkCall<Unit> {
                httpClient.post(HttpConstants.FCM_URL) {
                    parameter("token", token)
                }
            }
        }
    }

    private fun sendNotification(message: String, productId: String?) {
        val uri = "foodsaver://app/productDetails/$productId/true".toUri()

        val requestCode = 0

        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            `package` = "com.foodsaver.app"
            action = Intent.ACTION_VIEW
            data = uri
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this.applicationContext,
            productId?.hashCode() ?: requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationCompat = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(CONTENT_TITLE)
            .setSmallIcon(R.drawable.splash)
            .setContentText(message)
            .setSound(ringtone)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(NotificationManager::class.java)

        val channel = NotificationChannel(
            CHANNEL_ID,
            "FoodSaver",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notificationId = 0
        notificationManager.notify(notificationId, notificationCompat.build())
    }

    companion object {
        const val CONTENT_TITLE = "FoodSaver"
        const val CHANNEL_ID = "foodsaver-fcm-channel-name"
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}