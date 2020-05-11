package com.example.pocketgardener

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlantAdviceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.individual_advice)
        val timeId : TextView = findViewById(R.id.timeText)
        val seasonId : TextView = findViewById(R.id.seasonText)
        val nameId : TextView = findViewById(R.id.plant_header)
        val name = intent.getStringExtra("plant_name")
        val time = intent.getStringExtra("plant_time")
        val season = intent.getStringExtra("plant_season")

        nameId.text = name?.toString()
        seasonId.text = season?.toString()
        timeId.text = time?.toString()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}