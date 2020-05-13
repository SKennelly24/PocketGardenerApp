package com.example.pocketgardener

import android.os.AsyncTask
import android.util.Log
import com.example.pocketgardener.Utilities.getJson
import com.example.pocketgardener.Utilities.parameterizeUrl
import org.json.JSONException
import java.lang.ref.WeakReference
import java.net.URL

const val URL = "https://www.growstuff.org/api/v1/crops?sort=median_days_to_first_harvest"

class AdviceDownloader(activity: AdviceFragment): AsyncTask<URL, Void, List<PlantItem>>() {
    private val context = WeakReference(activity)

    override fun doInBackground(vararg params: URL?): List<PlantItem> {
        val plantList : List<PlantItem>
        plantList = try {
            val result = getJson(parameterizeUrl(URL))
            val plantsJSON = result.getJSONArray("data")
            (0 until plantsJSON.length()).map { i ->
                val plants = plantsJSON.getJSONObject(i)
                val id = plants.getString("id")
                val image_url = "https://www.growstuff.org/api/v1/photos/$id"
                val plant_attributes = plants.getJSONObject("attributes")
                val time = plant_attributes.getInt("median-days-to-first-harvest")
                PlantItem(plant_attributes.getString("name"), time.toString(), plant_attributes.getString("en-wikipedia-url"), image_url)
            }
        }catch (e: JSONException) {
            Log.d("Error", "AsyncTask")
            listOf()
        }
        return plantList
    }

    override fun onPostExecute(result: List<PlantItem>?) {
        super.onPostExecute(result)
        if (result != null) {
            context.get()?.plantList = result
        }
    }
}

