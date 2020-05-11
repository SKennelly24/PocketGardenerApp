package com.example.pocketgardener

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class AdviceFragment : Fragment() {
    private lateinit var plantPicker: RecyclerView
    private var plantList: List<PlantItem> = listOf(
        PlantItem("Carrots", "summer", "1 week",
            "https://cdn.shopify.com/s/files/1/2971/2126/products/Carrot_2000x.jpg?v=1528770261"))
    private lateinit var adapter: PlantAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_advice, container, false)
        plantPicker = view.findViewById(R.id.PlantRecycler)

        initRecyclerView()

        return view
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        plantPicker.layoutManager = layoutManager

        //Adding a divider decoration
        val decoration = DividerItemDecoration(context, layoutManager.orientation)
        plantPicker.addItemDecoration(decoration)

        //Connect adapter to the recycler view
        adapter = context?.let { context ->
            PlantAdapter(context, plantList) {
                launchIndividualActivity(it)
            }
        }!!
        plantPicker.adapter = adapter
    }

    private fun launchIndividualActivity(plant: PlantItem) {
        val intent = Intent(context, PlantAdviceActivity::class.java)
        intent.putExtra("plant_name", plant.name)
        intent.putExtra("plant_time", plant.time)
        intent.putExtra("plant_season", plant.season)
        startActivity(intent)
    }
}