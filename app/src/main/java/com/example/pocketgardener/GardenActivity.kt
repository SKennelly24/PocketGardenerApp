package com.example.pocketgardener

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GardenActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.individual_plant)
        val nameTextView : TextView = findViewById(R.id.plant_header)
        val name = intent.getStringExtra("garden_name")
        Log.d("Plant", name)
        nameTextView.text = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
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
    }

    private fun takePhoto() {
        Log.d("ActionBar", "Take Photo")
    }

    private fun uploadPhoto() {
        Log.d("ActionBar", "Upload Photo")
    }



}
