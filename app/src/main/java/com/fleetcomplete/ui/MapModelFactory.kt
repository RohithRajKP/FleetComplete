package com.fleetcomplete.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fleetcomplete.repository.MapRepo
import com.fleetcomplete.ui.viewmodels.MapsViewModel


@Suppress("UNCHECKED_CAST")

class MapModelFactory(
    private val repository: MapRepo
) : ViewModelProvider.NewInstanceFactory(){

   override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MapsViewModel(repository) as T
    }

}