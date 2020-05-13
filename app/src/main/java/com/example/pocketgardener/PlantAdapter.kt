package com.example.pocketgardener

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PlantAdapter(
    private val context: Context,
    private val plant_list: List<PlantItem>,
    val clickListener: (PlantItem) -> Unit): RecyclerView.Adapter<PlantViewHolder>() {
    override fun getItemCount(): Int = plant_list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.plant_advice_item, parent, false)
        val holder = PlantViewHolder(view)

        view.setOnClickListener {
            clickListener(plant_list[holder.adapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: PlantViewHolder, i: Int) {
        holder.name.text = plant_list[i].name
        //Picasso.get().load(plant_list[i].image_url).into(holder.image)

        /*when(plant_list[i].season) {
            "winter" -> holder.season.setImageResource(R.drawable.winter)
            "autumn" -> holder.season.setImageResource(R.drawable.autmn)
            "summer" -> holder.season.setImageResource(R.drawable.summer)
            "spring" -> holder.season.setImageResource(R.drawable.spring)
        }*/

        holder.time.text = plant_list[i].time
    }
}