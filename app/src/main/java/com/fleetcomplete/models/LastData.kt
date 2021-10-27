package com.fleetcomplete.models

data class LastData(
    val meta: Meta,
    val response: List<Response>,
    val status: Int
)