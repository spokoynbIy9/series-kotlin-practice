package com.example.series_kotlin_hm.profile.feature.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.series_kotlin_hm.profile.feature.R

interface MainActivityIntentProvider {
    fun getMainActivityIntent(context: Context): Intent
}

object NotificationHelper {
    private const val CHANNEL_ID = "class_reminder_channel"
    private const val CHANNEL_NAME = "Напоминания о парах"
    private const val NOTIFICATION_ID = 1

    private var intentProvider: MainActivityIntentProvider? = null

    fun setIntentProvider(provider: MainActivityIntentProvider) {
        intentProvider = provider
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления о начале любимой пары"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showClassReminderNotification(context: Context, fullName: String) {
        createNotificationChannel(context)

        val intent = intentProvider?.getMainActivityIntent(context) ?: run {
            // Fallback: создаем базовый Intent
            Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Начало любимой пары!")
            .setContentText("$fullName, пора на пару по мобильной разработке!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}

