package com.example.pocketgardener

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class TimePickerFragment: DialogFragment() {
    var listener: TimePickerDialog.OnTimeSetListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(activity, listener, 6, 0, false)
    }
}