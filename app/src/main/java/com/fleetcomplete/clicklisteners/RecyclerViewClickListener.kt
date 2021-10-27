package com.fleetcomplete.clicklisteners

import android.view.View
import com.fleetcomplete.models.Response


interface RecyclerViewClickListener {
    fun onRecyclerViewItemClick(fleet: Response, position:Int)
}

