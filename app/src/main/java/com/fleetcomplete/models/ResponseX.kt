package com.fleetcomplete.models

data class ResponseX(
    val DeltaDistance: Any,
    val Direction: Any,
    val DriverId: Any,
    val EngineStatus: String,
    val EventType_dec: String,
    val GPSState: String,
    val GreenDrivingType: Any,
    val GreenDrivingValue: Any,
    val Latitude: Double,
    val Longitude: Double,
    val Power: Double,
    val Satellites: String,
    val ServerGenerated: Any,
    val Speed: Any,
    val SplitSegment: Any,
    val timestamp: String
)