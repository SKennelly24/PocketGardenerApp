package com.example.pocketgardener

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import org.json.JSONObject
import java.io.BufferedInputStream
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import javax.net.ssl.HttpsURLConnection

object Utilities {
    fun scheduleReminder(context: Context, hour: Int, minute: Int) {
        val today = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.RTC, today.timeInMillis, AlarmManager.INTERVAL_DAY, reminderIntent(context))
    }

    fun reminderIntent(context: Context) : PendingIntent{
        return Intent(context, NotificationReceiver::class.java).let {
            PendingIntent.getBroadcast(context, 0, it, 0)
        }
    }

    fun getJson(url: URL): JSONObject {
        val connection = url.openConnection() as HttpsURLConnection
        try {
            val json = BufferedInputStream(connection.inputStream).readBytes().toString(Charset.defaultCharset())
            return JSONObject(json)
        } finally {
            connection.disconnect()
        }
    }

    fun parameterizeUrl(url: String): URL {
        val builder = Uri.parse(url).buildUpon()
        val uri = builder.build()
        return URL(uri.toString())
    }

}