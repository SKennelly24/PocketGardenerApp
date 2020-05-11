package com.example.pocketgardener

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class GardenActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener{
    private lateinit var name : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.individual_plant)
        val nameTextView : TextView = findViewById(R.id.plant_header)
        val plantedTextView : TextView = findViewById(R.id.plantedText)
        val commentTextView : TextView = findViewById(R.id.commentText)
        name = intent.getStringExtra("name")
        val planted = intent.getStringExtra("planted")
        val comments = intent.getStringExtra("comments")
        nameTextView.text = name
        plantedTextView.text = planted
        commentTextView.text = comments
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        createNotificationChannel()

    }

    override fun onTimeSet(picker: TimePicker, hour: Int, minute: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().apply {
            putInt("hour", hour)
            putInt("minute", minute)
            apply()
        }

        Utilities.scheduleReminder(applicationContext, hour, minute)

        val receiver = ComponentName(this, BootReceiver::class.java)
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)



    }

    private fun setReminderTime() {
        val fragment = TimePickerFragment()
        fragment.listener = this
        fragment.show(supportFragmentManager,null)


    }
    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Notification.CATEGORY_REMINDER, "Weekly Reminder", importance).apply {
            description = "Take a photo of your $name"
        }
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.garden_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.shareButton -> {
            share()
            true
        }
        R.id.reminderButton -> {
            setReminder()
            true
        }
        R.id.takePhotoButton -> {
            takePhoto()
            true
        }
        R.id.uploadPhotoButton -> {
            uploadPhoto()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun share() {
        Log.d("ActionBar", "Share Progress")
    }

    private fun setReminder() {
        Log.d("ActionBar", "Set Reminder")
        setReminderTime()
    }

    private fun takePhoto() {
        Log.d("ActionBar", "Take Photo")
    }

    private fun uploadPhoto() {
        Log.d("ActionBar", "Upload Photo")
    }



}
