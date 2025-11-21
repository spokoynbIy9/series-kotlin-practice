package com.example.series_kotlin_hm.presentation.profile.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.series_kotlin_hm.presentation.profile.notification.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val fullName = intent.getStringExtra(EXTRA_FULL_NAME) ?: ""
        NotificationHelper.showClassReminderNotification(context, fullName)
    }

    companion object {
        const val EXTRA_FULL_NAME = "extra_full_name"
        const val ACTION_ALARM = "com.example.series_kotlin_hm.ACTION_ALARM"
    }
}

