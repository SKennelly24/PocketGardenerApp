package com.example.pocketgardener

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PhotoHolder(view: View):  RecyclerView.ViewHolder(view){
    val dateLabel: TextView = view.findViewById(R.id.date_label)
    val photoView: ImageView = view.findViewById(R.id.plant_image)
}