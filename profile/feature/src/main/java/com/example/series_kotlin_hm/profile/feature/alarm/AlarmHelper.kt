package com.example.series_kotlin_hm.profile.feature.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.series_kotlin_hm.profile.feature.receiver.AlarmReceiver
import java.util.Calendar

object AlarmHelper {
    private const val REQUEST_CODE = 1001

    fun scheduleAlarm(context: Context, hour: Int, minute: Int, fullName: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // По заданию: дата срабатывания - сегодняшний день
            // Если время уже прошло сегодня, устанавливаем на завтра
            // Но можно также установить на сегодня, если прошло немного времени
            if (timeInMillis <= System.currentTimeMillis()) {
                // Если время прошло более чем на 1 минуту, устанавливаем на завтра
                val diff = System.currentTimeMillis() - timeInMillis
                if (diff > 60000) { // более 1 минуты
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_ALARM
            putExtra(AlarmReceiver.EXTRA_FULL_NAME, fullName)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}

