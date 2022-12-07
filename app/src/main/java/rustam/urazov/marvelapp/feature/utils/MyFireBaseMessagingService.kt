package rustam.urazov.marvelapp.feature.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import rustam.urazov.marvelapp.R
import rustam.urazov.marvelapp.core.extention.empty
import rustam.urazov.marvelapp.feature.ui.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage.notification?.body ?: String.empty())
    }

    override fun onNewToken(token: String) {
        sendRegistrationToStorage(token)
    }

    private fun sendRegistrationToStorage(token: String?) {
        val settings = applicationContext.getSharedPreferences("FCMSettings", Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.putString("token", token).apply()
    }

    private fun sendNotification(messageBody: String) {
        sendBroadcast(Intent("id").putExtra("id", messageBody))
        val intent = Intent(this, MainActivity::class.java)
        intent.data = "marvel://characterDetails/${messageBody}".toUri()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Character")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.marvel)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}