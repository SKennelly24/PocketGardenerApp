package com.example.pocketgardener
import androidx.room.*

@Dao
interface YourPlantDao {
    @Insert
    fun insert(plant: YourPlant): Long

    @Update
    fun update(plant: YourPlant)

    @Delete
    fun delete(plant: YourPlant)

    @Query("SELECT * FROM your_plants")
    fun getAll(): List<YourPlant>

}