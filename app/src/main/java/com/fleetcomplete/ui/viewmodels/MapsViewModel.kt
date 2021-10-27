package com.fleetcomplete.ui.viewmodels

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fleetcomplete.models.ResponseX
import com.fleetcomplete.models.RowData
import com.fleetcomplete.repository.MapRepo
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat

class MapsViewModel(private val repository: MapRepo) : ViewModel() {
    private val _rowData = MutableLiveData<RowData>()
    val rowData: LiveData<RowData>
        get() = _rowData
    var _distance = MutableLiveData<Float>() //for calculating distance
    fun getDistance(listDistance: List<ResponseX>) {
        var Distnce: Float = 0f
        var k = listDistance.size
        for (i in 0..k - 1) {
            var lat: Double = listDistance[i].Latitude
            var long: Double = listDistance[i].Longitude
            if (i > 0 && i < k - 1) {
                val locationStart = Location("pointStart")
                locationStart.latitude = listDistance[i].Latitude
                locationStart.longitude = listDistance[i].Longitude
                val locationEnd = Location("pointEnd")
                locationEnd.latitude = listDistance[i - 1].Latitude
                locationEnd.longitude = listDistance[i - 1].Longitude
                Distnce = Distnce + locationStart.distanceTo(locationEnd)
            }
        }
        Distnce = Distnce / 1000.0f
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING

        _distance.value = df.format(Distnce).toFloat()
    }

    fun getData(beigntime: String, endtime: String, Objectid: Int, apiKey: String) =
        viewModelScope.launch {
            ApiCall(beigntime, endtime, Objectid, apiKey)
        }

    suspend fun ApiCall(beigntime: String, endtime: String, Objectid: Int, apiKey: String) {
        try {
            viewModelScope.launch {
                val response = repository.getRawData(beigntime, endtime, Objectid, apiKey)
                if (response.isSuccessful) {
                    _rowData.value = response.body()
                    //once got the response will calculate the distance on same couroutine.and update to the text view
                    response.body()?.let { getDistance(it.response) }
                }
            }
        } catch (Ex: Exception) {
        }
    }
}