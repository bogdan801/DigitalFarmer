package com.bogdan801.digitalfarmer.data.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlin.math.*

const val earthRadiusM: Double = 6372800.0

fun getTouchRadius(zoom: Float, scalar: Double = 1.0): Double
    = scalar * 2000000.0 * (0.5.pow(zoom.toDouble()))

fun LatLng.distanceTo(point: LatLng): Double {
    val dLat = Math.toRadians(point.latitude - latitude)
    val dLon = Math.toRadians(point.longitude - longitude)
    val originLat = Math.toRadians(latitude)
    val destinationLat = Math.toRadians(point.latitude)

    val a = sin(dLat / 2).pow(2.toDouble()) + sin(dLon / 2).pow(2.toDouble()) * cos(originLat) * cos(destinationLat)
    val c = 2 * asin(sqrt(a))
    return earthRadiusM * c
}

fun getPolygonCenterPoint(polygonPointsList: List<LatLng>): LatLng {
    val bounds = LatLngBounds.Builder().apply {
        polygonPointsList.forEach {
            include(it)
        }
    }.build()

    return bounds.center
}

fun openGoogleMapsAtLocation(context: Context, coordinateToOpen: LatLng) {
    val gmmIntentUri = Uri.parse("geo:${coordinateToOpen.latitude},${coordinateToOpen.longitude}")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps") // Use the Google Maps app

    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        // If Google Maps app is not available, you can open it in a web browser
        val mapUrl = "https://www.google.com/maps?q=${coordinateToOpen.latitude},${coordinateToOpen.longitude}"
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl))
        context.startActivity(webIntent)
    }
}