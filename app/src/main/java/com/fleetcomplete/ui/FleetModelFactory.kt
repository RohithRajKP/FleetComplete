package com.fleetcomplete.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.fleetcomplete.repository.FleetRepo
import com.fleetcomplete.ui.viewmodels.FleetViewModel


@Suppress("UNCHECKED_CAST")
class FleetModelFactory(
    private val app:Application,
    private val repository: FleetRepo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FleetViewModel(app, repository) as T
    }

}

