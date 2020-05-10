package com.example.pocketgardener

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GardenViewHolder (view : View) : RecyclerView.ViewHolder(view) {
    val image: ImageView = view.findViewById(R.id.garden_image)
    val name: TextView = view.findViewById(R.id.garden_name)
}