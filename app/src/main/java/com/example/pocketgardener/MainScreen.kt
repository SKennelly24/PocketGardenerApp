package com.example.pocketgardener

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * Screens available for display in the main screen, with their respective titles,
 * icons, and menu item IDs and fragments.
 */
enum class MainScreen(
    @IdRes val menuItemId: Int,
    @StringRes val titleStringId: Int,
    val fragment: Fragment
) {
    ADVICE(R.id.bottom_navigation_advice, R.string.navigation_advice, AdviceFragment()),
    GARDEN(R.id.bottom_navigation_garden, R.string.navigation_garden, GardenFragment())
}

fun getMainScreenForMenuItem(menuItemId: Int): MainScreen? {
    for (mainScreen in MainScreen.values()) {
        if (mainScreen.menuItemId == menuItemId) {
            return mainScreen
        }
    }
    return null
}