package com.example.pocketgardener

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PhotoAdapter(private val photos: List<PlantPhoto>) : RecyclerView.Adapter<PhotoHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plant_photo, parent, false)
        return PhotoHolder(view)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: PhotoHolder, i: Int) {
        holder.dateLabel.text = "${photos[i].day} / ${photos[i].month}"
        val bitmap = BitmapFactory.decodeFile(photos[i].file.absolutePath)
        holder.photoView.setImageBitmap(bitmap)

    }



}