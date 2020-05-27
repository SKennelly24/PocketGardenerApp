package com.example.pocketgardener

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import java.io.File

const val LOCAL_TAG = "Garden Adapter"

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
        if (gardenList[i].image != "") {
            val bitmap = BitmapFactory.decodeFile(gardenList[i].image)
            holder.image.setImageBitmap(bitmap)
            Log.d(LOCAL_TAG, "Adding photo")
        }
    }

    fun insert(plant: YourPlant) {
        Log.d("Garden List", gardenList.toString())
        Log.d("Plant", plant.name)
        if (database != null) {
            NewYourPlantTask(database!!, plant).execute()
            gardenList.add(plant)
            notifyItemInserted(gardenList.size -1)
        } else {
            Log.d(LOCAL_TAG, "Database null")
        }
    }

    fun delete(deleteIndex : Int) {
        val plant = gardenList.removeAt(deleteIndex)
        notifyItemRemoved(deleteIndex)
        DeleteYourPlantTask(database!!, plant).execute()

    }

    fun clear() {
        if (database != null) {
            gardenList.clear()
            notifyDataSetChanged()
            ClearDatabaseTask(database!!).execute()
        }
    }

    fun updateImage(plantName: String, image : String) {
        Log.d(LOCAL_TAG, "updating image")
        var updateIndex = -1
        for (i in gardenList.indices) {
            if (plantName == gardenList[i].name) {
                updateIndex = i
            }
        }
        Log.d(LOCAL_TAG, "Index is $updateIndex")
        if (updateIndex != -1) {
            gardenList[updateIndex].image = image
            Log.d(LOCAL_TAG, "Plant Image is: ${gardenList[updateIndex].image}")
            notifyItemChanged(updateIndex)
            UpdatePlantTask(database!!, gardenList[updateIndex]).execute()
        }
    }

}