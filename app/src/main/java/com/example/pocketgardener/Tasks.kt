package com.example.pocketgardener

import android.os.AsyncTask
import android.util.Log
import androidx.room.Room
import java.lang.ref.WeakReference

class LoadDatabaseTask(adapter: GardenAdapter) : AsyncTask<Unit, Unit, YourPlantDatabase?>() {
    private val adapter = WeakReference(adapter)

    override fun doInBackground(vararg params: Unit?): YourPlantDatabase? {
        Log.d("Garden Adapter", "Loading Data")
        var database: YourPlantDatabase? = null
        adapter.get()?.let {
            database = Room.databaseBuilder(it.context.applicationContext, YourPlantDatabase::class.java, "your_plants").fallbackToDestructiveMigration().build()
        }
        return database
    }
    override fun onPostExecute(database: YourPlantDatabase?) {
        adapter.get()?.let {
            it.database = database
        }
    }

}

class LoadPlantsTask(private val database: YourPlantDatabase, private val adapter: GardenAdapter) : AsyncTask<Unit, Unit, List<YourPlant>>() {
    override fun doInBackground(vararg params: Unit?): List<YourPlant> {
        Log.d("Garden Adapter", "Loading Plants")
        val yourPlantDao = database.yourPlantDao()
        return yourPlantDao.getAll()
    }

    override fun onPostExecute(plants: List<YourPlant>) {
        adapter.gardenList = plants.toMutableList()
    }

}

class NewYourPlantTask(private val database : YourPlantDatabase, private val yourPlant : YourPlant) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg params: Unit?) {
        yourPlant.id = database.yourPlantDao().insert(yourPlant)
    }
}

class DeleteYourPlantTask(private val database: YourPlantDatabase, private val plant: YourPlant) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg params: Unit?) {
        database.yourPlantDao().delete(plant)
    }
}

class ClearDatabaseTask(private val database: YourPlantDatabase): AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg params: Unit?) {
        database.clearAllTables()
    }
}

class UpdatePlantTask(private val database: YourPlantDatabase,
                     private val plant: YourPlant) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg p0: Unit?) {
        database.yourPlantDao().update(plant)
    }
}

