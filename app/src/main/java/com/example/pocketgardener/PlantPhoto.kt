package com.example.pocketgardener

import java.io.File

data class PlantPhoto(val file: File) {
    val name: String = file.parentFile.name
    val month: Int
    val day: Int

    init {
        val matches = "^(\\d+)_(\\d+)$".toRegex().matchEntire(file.nameWithoutExtension)
        if (matches != null) {
            month = matches.groupValues[1].toInt()
            day = matches.groupValues[2].toInt()
        } else {
            month = 1
            day = 1
        }
    }
}
