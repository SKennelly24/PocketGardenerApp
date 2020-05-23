package com.example.pocketgardener

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
/**
 * Class to implement a view holder for the plant items
 */
class PlantViewHolder (view : View) : RecyclerView.ViewHolder(view){
    val name: TextView = view.findViewById(R.id.plant_name)
    val time: TextView = view.findViewById(R.id.time_text)
}