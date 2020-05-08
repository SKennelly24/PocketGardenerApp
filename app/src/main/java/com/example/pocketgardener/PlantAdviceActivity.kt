package com.example.pocketgardener

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class PlantAdviceActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.individual_advice)
        val name = intent.getStringArrayExtra("plant_name")
        val time = intent.getStringArrayExtra("plant_time")
        val season = intent.getStringArrayExtra("plant_season")
        val time_id : TextView = findViewById(R.id.timeText)
        val season_id : TextView = findViewById(R.id.seasonText)
        val name_id : TextView = findViewById(R.id.plant_header)
        name_id.text = name?.toString()
        season_id.text = season?.toString()
        time_id.text = time?.toString()


    }
}