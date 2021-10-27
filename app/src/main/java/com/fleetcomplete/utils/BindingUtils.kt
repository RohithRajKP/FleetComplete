package com.fleetcomplete.utils

import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

/*
Calculating data age for databinding xml
 */

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("data_age")
fun estimateDataAge(
    view: TextView,
    timeStamp: String
) {
    var diffe: String = ""//diffrence
    try {

        val tz = TimeZone.getTimeZone("GMT+03:00")
        val c = Calendar.getInstance(tz)
        val timeStamp: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStamp)
        val currentTime = c.time
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDateandTime = sdf.format(currentTime)
        val curentdate: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentDateandTime)


        var different = curentdate.time - timeStamp.time //calculating the timediff

        println("startDate : $curentdate")
        println("endDate : $timeStamp")
        println("different : $different")
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        var elapsedyears: Int = 0
        val elapsedDays = different / daysInMilli //day difference
        if (elapsedDays > 365) {
            elapsedyears = (elapsedDays / 365).toInt() //year diffrence
        }
        different = different % daysInMilli

        val elapsedHours = different / hoursInMilli //hour difference
        different = different % hoursInMilli

        val elapsedMinutes = different / minutesInMilli //min difference
        different = different % minutesInMilli

        val elapsedSeconds = different / secondsInMilli //seconds difference
        if (elapsedyears > 0) {
            diffe = elapsedyears.toString() + "y"
        } else if (elapsedDays > 0) {
            diffe = elapsedDays.toString() + "d"
        } else if (elapsedHours > 0) {
            diffe = elapsedHours.toString() + "h"
        } else if (elapsedMinutes > 0) {
            diffe = elapsedMinutes.toString() + "m"
            if (elapsedSeconds > 0) {
                diffe = diffe + elapsedSeconds.toString() + "s"
            }
        } else if (elapsedSeconds > 0) {
            diffe = elapsedSeconds.toString() + "s"
        }
        view.setText(diffe + " ago")
        diffe = ""
    } catch (Ex: Exception) {
        Ex.printStackTrace()
    }


}

