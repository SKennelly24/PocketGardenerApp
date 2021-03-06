package com.example.pocketgardener

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream

class GardenFragment : Fragment() {
    private lateinit var gardenPicker : RecyclerView
    private lateinit var adapter: GardenAdapter
    private lateinit var dialogView : View
    private lateinit var prefs: SharedPreferences
    private var player: MediaPlayer? = null


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
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
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
            animateThing(i)
        }
        builder?.setPositiveButton("OK") { _, _->
            adapter.delete(index)
        }
        builder?.setNegativeButton("Cancel", null)
        builder?.create()?.show()
    }

    private fun animateThing(index: Int) {
        val viewHolder : GardenViewHolder = gardenPicker.findViewHolderForAdapterPosition(index) as GardenViewHolder
        val animation = AnimationUtils.loadAnimation(context, R.anim.sample_animation)
        viewHolder.image.startAnimation(animation)
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
            val name = editName.text.toString().capitalize()
            val planted = editPlanted.text.toString()
            val comments = editComments.text.toString()
            addPlant(name, planted, comments)
        }
        builder?.setNegativeButton("Cancel", null)
        builder?.create()?.show()
    }

    private fun addPlant(name: String, planted: String, comments: String) {
        adapter.insert(YourPlant(name, planted, comments, ""))
        play()

    }
    private fun play() {
        releasePlayer()

        val file = File.createTempFile("ding_", ".ding", context?.cacheDir)
        writeSongToFile(file)
        player = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI).apply {
            start()
            setOnCompletionListener {
                releasePlayer()
                file.delete()
            }
        }

    }

    fun writeSongToFile(file: File) {
        val title = ""
        val beatsPerMinute = 100
        val notes = "4c4 4c4 4g4 4g4 4a4 4a4 2g4"
        PrintStream(FileOutputStream(file)).apply {
            println("$title:d=4,o=5,b=$beatsPerMinute:${notes.split("\\s+".toRegex()).joinToString(",")}")
            close()
        }
    }

    private fun releasePlayer() {
        player?.stop()
        player?.release()
        player = null
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }





    private fun initRecyclerView() {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val layoutManager = GridLayoutManager(context, 4)
            gardenPicker.layoutManager = layoutManager
        } else {
            val layoutManager = GridLayoutManager(context, 2)
            gardenPicker.layoutManager = layoutManager
        }
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
        val notificationsOn = prefs.getBoolean("notifications", true)
        Log.d("GardenFragment", "Notifications: $notificationsOn")
        intent.putExtra("notifications", notificationsOn)

        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("GardenFragment", "On activity result")
        when(requestCode) {
            1 -> {
                val name = data?.getStringExtra("name")
                Log.d("GardenFragment", "Returned $name")
                if (name != "" && name != null) {
                    val image = data?.getStringExtra("image")
                    Log.d("GardenFragment", "Image: $image")
                    adapter.updateImage(name, image)
                }
            }
        }
    }
}