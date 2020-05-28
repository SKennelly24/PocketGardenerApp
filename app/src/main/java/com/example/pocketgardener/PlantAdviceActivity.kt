package com.example.pocketgardener

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlantAdviceActivity : AppCompatActivity() {
    lateinit var plant_image : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.individual_advice)

        val timeId : TextView = findViewById(R.id.timeText)
        val nameId : TextView = findViewById(R.id.plant_header)
        val urlId : TextView = findViewById(R.id.info_url)
        plant_image = findViewById(R.id.plant_image)

        val name = intent.getStringExtra("plant_name")
        val time = intent.getStringExtra("plant_time")
        val wikiUrl = intent.getStringExtra("wiki_url")
        val growUrl = intent.getStringExtra("grow_url")

        ImageDownloader(this, growUrl).execute()
        nameId.text = name?.toString()
        timeId.text = time?.toString()

        urlId.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikiUrl))
            startActivity(intent)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}