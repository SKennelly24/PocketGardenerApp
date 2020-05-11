package com.example.pocketgardener

import android.os.Bundle
import android.util.Log
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
}
