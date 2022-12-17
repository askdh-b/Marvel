package rustam.urazov.marvelapp.feature.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import rustam.urazov.marvelapp.R
import rustam.urazov.marvelapp.core.extention.empty
import rustam.urazov.marvelapp.feature.ui.MainActivity
import rustam.urazov.marvelapp.feature.ui.MarvelDestinations.BASE_ROUTE
import rustam.urazov.marvelapp.feature.ui.MarvelDestinations.CHARACTER_DETAILS_ROUTE

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var settings: SharedPreferences

    companion object {
        const val FCMSETTINGS = "FCMSettings"
        const val TOKEN = "token"
    }

    override fun onCreate() {
        super.onCreate()
        settings = applicationContext.getSharedPreferences(FCMSETTINGS, Context.MODE_PRIVATE)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage.data["id"] ?: String.empty())
    }

    override fun onNewToken(token: String) {
        sendRegistrationToStorage(token)
    }

    private fun sendRegistrationToStorage(token: String?) {
        val editor = settings.edit()
        editor.putString(TOKEN, token).apply()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.data = "${BASE_ROUTE}${CHARACTER_DETAILS_ROUTE}/${messageBody}".toUri()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        }

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

        notificationManager.notify(0, notificationBuilder.build())
    }
}