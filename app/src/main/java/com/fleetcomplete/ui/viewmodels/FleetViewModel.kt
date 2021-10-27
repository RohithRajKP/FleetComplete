package com.fleetcomplete.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fleetcomplete.di.Kodein.App
import com.fleetcomplete.models.LastData
import com.fleetcomplete.repository.FleetRepo
import com.fleetcomplete.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class FleetViewModel(app: Application,
                     private val repository: FleetRepo
) : AndroidViewModel(app){
    ///for getting api key on alertdialoge box
    val mAPIKEY = MutableLiveData<String>()

    fun getAPIKEY(apikey: String) {
        mAPIKEY.value = apikey

    }
    /// for api call to retrieve vehicle details.
    val _lastData: MutableLiveData<Resource<LastData>> =
        MutableLiveData()

    fun getData(Key: String) = viewModelScope.launch {
        ApiCall(Key) //getting vehicle details.
    }

    private suspend fun ApiCall(Key: String) {
        _lastData.postValue(Resource.Loading())
        try {
            if (isInternet()) {
                val response = repository.getLastData(Key)
                _lastData.postValue(handleResponse(response))
            } else {
                _lastData.postValue(Resource.Error("No connection to the network"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _lastData.postValue(Resource.Error("Network Failure"))
                else -> _lastData.postValue(Resource.Error("Error " + t.toString()))
            }
        }
    }
    //response is handled in sealed class resources.
    private fun handleResponse(response: Response<LastData>): Resource<LastData> {  //handling response
        if (response.isSuccessful) {
            val apiresponse: LastData? = null
            response.body()?.let { resultResponse ->
                return Resource.Success(apiresponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private fun isInternet(): Boolean {
        val connectivityManager = getApplication<App>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    ConnectivityManager.TYPE_WIFI -> true
                    else -> false
                }
            }
        }
        return false
    }
}

