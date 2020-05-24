package com.example.pocketgardener

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "your_plants")
class YourPlant(var name: String, var planted: String, var comments: String, var image: String) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
    override fun toString(): String {
        return "$name $planted $comments"
    }
}