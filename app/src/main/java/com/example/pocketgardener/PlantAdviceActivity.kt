package com.example.pocketgardener

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class PlantAdviceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.individual_advice)
        val time_id : TextView = findViewById(R.id.timeText)
        val season_id : TextView = findViewById(R.id.seasonText)
        val name_id : TextView = findViewById(R.id.plant_header)
        val name = intent.getStringExtra("plant_name")
        val time = intent.getStringExtra("plant_time")
        val season = intent.getStringExtra("plant_season")

        name_id.text = name?.toString()
        season_id.text = season?.toString()
        time_id.text = time?.toString()

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
    }
}