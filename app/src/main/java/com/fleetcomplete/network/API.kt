package com.fleetcomplete.network

import com.fleetcomplete.models.LastData
import com.fleetcomplete.models.RowData
import com.fleetcomplete.utils.Constants.Companion.BASE_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

    ///for loading vehicle details (first screen)

    //https://app.ecofleet.com/seeme/Api/Vehicles/getLastData?key=home.assignment.2-1230927&json=true
    @GET("getLastData")
    suspend fun getLastData(

        @Query("key") key: String,
        @Query("json") json: Boolean = true
    ): Response<LastData>

    //for getting trip details(second screen).

    //https://app.ecofleet.com/seeme/Api/Vehicles/getRawData?begTimestamp=2021-10-11T00:00:00.000Z&endTimestamp=2021-10-11T11:59:59.000Z&objectId=187286&key=home.assignment.2-1230927&json=true
    @GET("getRawData")
    suspend fun getRawData(
        @Query("begTimestamp") begTimestamp: String,
        @Query("endTimestamp") endTimestamp: String,
        @Query("objectId") objectId: Int,
        @Query("key") key: String,
        @Query("json") json: Boolean = true
    ): Response<RowData>

    companion object {
        operator fun invoke(): API {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(API::class.java)
        }
    }
}