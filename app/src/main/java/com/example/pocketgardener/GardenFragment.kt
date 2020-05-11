package com.example.pocketgardener

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class GardenFragment : Fragment() {
    private lateinit var gardenPicker : RecyclerView
    private var gardenList : List<YourPlant> = listOf(
        YourPlant("Carrots", "1st April", "they look great"))
    private lateinit var adapter: GardenAdapter
    private lateinit var dialogView : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialogView = inflater.inflate(R.layout.new_plant_dialog, container, false)
        val view = inflater.inflate(R.layout.fragment_garden, container, false)
        gardenPicker = view.findViewById(R.id.garden_recycler)
        initRecyclerView()
        val clearPlantButton : Button = view.findViewById(R.id.clearButton)
        clearPlantButton.setOnClickListener {
            adapter.clear()
        }
        val deletePlantButton : Button = view.findViewById(R.id.deleteButton)
        deletePlantButton.setOnClickListener {
            deletePlantDialog()
        }
        val newPlantButton: Button = view.findViewById(R.id.new_plant_button)
        newPlantButton.setOnClickListener {
            newPlantDialog()
        }
        return view
    }

    private fun getPlantNames(plants: List<YourPlant>) :Array<String>{
        val plantNames :ArrayList<String> = arrayListOf()
        for (plant in plants) {
            plantNames.add(plant.name)
        }
        return plantNames.toTypedArray()
    }

    private fun deletePlantDialog() {
        var index = -1
        val plantNames = getPlantNames(adapter.gardenList)
        val builder = context?.let { androidx.appcompat.app.AlertDialog.Builder(it) }
        builder?.setTitle("Delete a Plant")
        builder?.setSingleChoiceItems(plantNames, -1) {_, i ->
            index = i
        }
        builder?.setPositiveButton("OK") { _, _->
            adapter.delete(index)
        }
        builder?.setNegativeButton("Cancel", null)
        builder?.create()?.show()
    }

    private fun newPlantDialog() {
        if (dialogView.parent != null) {
            (dialogView.parent as ViewGroup).removeView(dialogView)
        }
        val editName : EditText = dialogView.findViewById(R.id.edit_plant_name)
        val editPlanted : EditText = dialogView.findViewById(R.id.edit_planted)
        val editComments : EditText = dialogView.findViewById(R.id.edit_comments)
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setView(dialogView)
        builder?.setPositiveButton("Ok") { _, _ ->
            val name = editName.text.toString()
            val planted = editPlanted.text.toString()
            val comments = editComments.text.toString()
            addPlant(name, planted, comments)
        }
        builder?.setNegativeButton("Cancel", null)
        builder?.create()?.show()
    }

    private fun addPlant(name: String, planted: String, comments: String) {
        adapter.insert(YourPlant(name, planted, comments))
    }



    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(context, 2)
        gardenPicker.layoutManager = layoutManager
        adapter = context?.let { context ->
            GardenAdapter(context) {
                launchIndividualGarden(it)
            }
        }!!
        gardenPicker.adapter = adapter
    }

    private fun launchIndividualGarden(gardenItem : YourPlant) {
        Log.d("Plant", gardenItem.name)
        val intent = Intent(context, GardenActivity::class.java)
        intent.putExtra("name", gardenItem.name)
        intent.putExtra("planted", gardenItem.planted)
        intent.putExtra("comments", gardenItem.comments)
        startActivity(intent)
    }
}