package com.fleetcomplete.models

data class RowData(
    val meta: MetaX,
    val response: List<ResponseX>,
    val status: Int
)