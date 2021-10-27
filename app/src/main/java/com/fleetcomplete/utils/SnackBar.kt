package com.fleetcomplete.utils

import android.app.Activity
import com.google.android.material.snackbar.Snackbar

class SnackBars {

    fun showSnackBar(message: String?, activity: Activity?) {
        if (null != activity && null != message) {
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}