package com.example.pocketgardener

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi

fun Bundle.toParamsString() = keySet().map { "$it -> ${get(it)}" }.joinToString("\n")

@RequiresApi(Build.VERSION_CODES.O)
class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val intent: PendingIntent = Intent(context, MainActivity::class.java).run {
            this.putExtra("isAdd", true)
            PendingIntent.getActivity(context, 0, this, 0)
        }

        val notification = Notification.Builder(context, Notification.CATEGORY_REMINDER).run {
            setSmallIcon(R.drawable.water_can)
            setContentTitle("Don't forget about your plants")
            setContentText("Just a friendly reminder to water your plants.")
            setContentIntent(intent)
            setAutoCancel(true)
            build()
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, notification)

    }

}