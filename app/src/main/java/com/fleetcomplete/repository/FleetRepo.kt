package com.fleetcomplete.repository

import com.fleetcomplete.network.API


class FleetRepo(
    private val api: API,
) {

    suspend fun getLastData(API_KEY:String) = api.getLastData(API_KEY) //getting vehicle screen
}