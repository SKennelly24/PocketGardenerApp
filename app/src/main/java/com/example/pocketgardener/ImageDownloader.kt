package com.example.pocketgardener

import android.os.AsyncTask
import android.util.Log
import com.squareup.picasso.Picasso
import org.json.JSONException
import java.io.FileNotFoundException
import java.lang.ref.WeakReference
import java.net.URL

class ImageDownloader(
    activity: PlantAdviceActivity, private val growUrl: String): AsyncTask<Unit, Void, String>() {
    private val context = WeakReference(activity)
    override fun doInBackground(vararg params: Unit?): String? {
        val imageUrl = try {
            val result = Utilities.getJson(Utilities.parameterizeUrl(growUrl))
            val plantJSON = result.getJSONObject("data")
            val plant_attributes = plantJSON.getJSONObject("attributes")
            plant_attributes.getString("fullsize-url")

        } catch(e : JSONException) {
            Log.d("Error", "AsyncTask")
            ""
        } catch(e: FileNotFoundException) {
            ""
        }
        return imageUrl
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result != null && result != "") {
             Picasso.get().load(result).into(context.get()?.plant_image)
        }

    }
}
