package com.example.pocketgardener

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GardenFragment : Fragment() {
    private lateinit var gardenPicker : RecyclerView
    private var garden_list : List<GardenItem> = listOf(GardenItem("Carrots", "carrot"))
    private lateinit var adapter: GardenAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_garden, container, false)
        gardenPicker = view.findViewById(R.id.garden_recycler)
        initRecyclerView()
        return view
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(context, 2)
        gardenPicker.layoutManager = layoutManager
        adapter = context?.let { context ->
            GardenAdapter(context, garden_list) {
                launchIndividualGarden(it)
            }
        }!!
        gardenPicker.adapter = adapter
    }

    private fun launchIndividualGarden(gardenItem : GardenItem) {
        Log.d("Plant", gardenItem.name)
        val intent = Intent(context, GardenActivity::class.java)
        intent.putExtra("garden_name", gardenItem.name)
        startActivity(intent)
    }
}