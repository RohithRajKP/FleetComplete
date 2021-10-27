package com.fleetcomplete.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

/*
added parcel for communicate between fragments(navigation arguments).
RawValue for data type any
*/
@Parcelize
data class Response(

    val CANDistance: @RawValue Any? = null,
    val CANRPM: Int,
    val EVDistanceRemaining: @RawValue Any? = null,
    val EVStateOfCharge: @RawValue Any? = null,
    val EventType: String,
    val GreenDrivingType: Int,
    val GreenDrivingValue: Int,
    val address: String,
    val addressArea: Boolean,
    val addressAreaId: @RawValue Any? = null,
    val available: @RawValue Any? = null,
    val currentOdometer: @RawValue Any? = null,
    val currentWorkhours: @RawValue Any? = null,
    val customValues: List<@RawValue Any?>,
    val direction: Int,
    val displayColor: @RawValue Any? = null,
    val driverId: Int,
    val driverIsOnDuty: Boolean,
    val driverKey: @RawValue Any? = null,
    val driverName: String,
    val driverPhone: @RawValue Any? = null,
    val driverStatuses: List<@RawValue Any?>,
    val dutyTags: List<@RawValue Any?>,
    val employeeId: @RawValue Any? = null,
    val enforcePrivacyFilter: @RawValue Any? = null,
    val enginestate: Int,
    val externalId: @RawValue Any? = null,
    val fuel: @RawValue Any? = null,
    val gpsstate: Boolean,
    val inPrivateZone: Boolean,
    val lastEngineOnTime: String,
    val latitude: Double,
    val longitude: Double,
    val objectId: Int,
    val objectName: String,
    val offWorkSchedule: Boolean,
    val orgId: Int,
    val pairedObjectId: @RawValue Any? = null,
    val pairedObjectName: @RawValue Any? = null,
    val plate: String,
    val power: Double,
    val speed: Int,
    val tcoCardIsPresent: Boolean,
    val tcoData: @RawValue Any? = null,
    val timestamp: String,
    val tripPurposeDinSet: @RawValue Any? = null,
) : Parcelable