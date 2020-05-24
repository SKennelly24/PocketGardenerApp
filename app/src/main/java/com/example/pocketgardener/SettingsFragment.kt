package com.example.pocketgardener

import android.content.Context
import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val notificationPreference: SwitchPreferenceCompat? = findPreference("notifications")
    }
}