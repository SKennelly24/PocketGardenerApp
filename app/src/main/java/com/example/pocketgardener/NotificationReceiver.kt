package com.example.pocketgardener

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        if (prefs.getInt("hour", -1) >= 0) {
            Utilities.scheduleReminder(context, prefs.getInt("hour", 6), prefs.getInt("minute", 0))
        }

    }
}