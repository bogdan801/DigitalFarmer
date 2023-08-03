package com.bogdan801.digitalfarmer.data.util

import com.google.android.gms.maps.model.LatLng
import kotlin.math.*

const val earthRadiusM: Double = 6372800.0

fun getZoomIndependentCircleRadius(zoom: Float, scalar: Double = 1.0): Double
    = scalar * 1000000.0 * (0.5.pow(zoom.toDouble()))

fun LatLng.distanceTo(point: LatLng): Double {
    val dLat = Math.toRadians(point.latitude - latitude)
    val dLon = Math.toRadians(point.longitude - longitude)
    val originLat = Math.toRadians(latitude)
    val destinationLat = Math.toRadians(point.latitude)

    val a = sin(dLat / 2).pow(2.toDouble()) + sin(dLon / 2).pow(2.toDouble()) * cos(originLat) * cos(destinationLat)
    val c = 2 * asin(sqrt(a))
    return earthRadiusM * c
}