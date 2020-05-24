package com.example.pocketgardener

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [YourPlant::class], version = 2)
abstract class YourPlantDatabase : RoomDatabase() {
    abstract fun yourPlantDao() : YourPlantDao
}