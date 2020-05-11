package com.example.pocketgardener

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class GardenAdapter(
    private val context: Context,
    private val garden_list: List<GardenItem>,
    val clickListener: (GardenItem) -> Unit): RecyclerView.Adapter<GardenViewHolder>() {

    override fun getItemCount(): Int = garden_list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GardenViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.garden_item, parent, false)
        val holder = GardenViewHolder(view)

        view.setOnClickListener {
            clickListener(garden_list[holder.adapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: GardenViewHolder, i: Int) {
        holder.name.text = garden_list[i].name
    }

}