package com.fleetcomplete.repository

import com.fleetcomplete.network.API

class MapRepo (private val api: API,
) {

    suspend fun getRawData(
        begTimestamp: String,
        endTimestamp:String,
        objectId:Int,
        apiKey:String

    ) = api.getRawData(begTimestamp,endTimestamp,objectId,apiKey) //trip details screen api
}