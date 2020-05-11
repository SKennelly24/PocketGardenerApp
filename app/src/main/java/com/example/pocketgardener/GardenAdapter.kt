package com.example.pocketgardener

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class GardenAdapter(
    val context: Context,
    val clickListener: (YourPlant) -> Unit): RecyclerView.Adapter<GardenViewHolder>() {
    private var recyclerView: RecyclerView? = null

    var gardenList: MutableList<YourPlant> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var database: YourPlantDatabase? = null
        set(value) {
            field = value
            value?.let {
                LoadPlantsTask(it, this).execute()
            }
        }

    init {
        LoadDatabaseTask(this).execute()
    }
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun getItemCount(): Int = gardenList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GardenViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.garden_item, parent, false)
        val holder = GardenViewHolder(view)

        view.setOnClickListener {
            clickListener(gardenList[holder.adapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: GardenViewHolder, i: Int) {
        holder.name.text = gardenList[i].name
    }

    fun insert(plant: YourPlant) {
        Log.d("Garden List", gardenList.toString())
        Log.d("Plant", plant.name)
        if (database != null) {
            NewYourPlantTask(database!!, plant).execute()
            gardenList.add(plant)
            notifyItemInserted(gardenList.size -1)
        } else {
            Log.d("Garden Adapter", "Database null")
        }
    }




}