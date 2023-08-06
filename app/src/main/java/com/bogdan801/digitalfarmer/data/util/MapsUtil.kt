package com.bogdan801.digitalfarmer.data.util

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
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

fun getPolygonCenterPoint(polygonPointsList: List<LatLng>): LatLng {
    val bounds = LatLngBounds.Builder().apply {
        polygonPointsList.forEach {
            include(it)
        }
    }.build()

    return bounds.center
}

private data class Point(val x: Double, val y: Double)
fun getClosestPointToASegment(p: LatLng, a: LatLng, b: LatLng): LatLng {
    val ab = Point(b.latitude - a.latitude, b.longitude - a.longitude)
    val ap = Point(p.latitude - a.latitude, p.longitude - a.longitude)

    val dotProduct = (ap.x * ab.x) + (ap.y * ab.y)
    val abLengthSquared = (ab.x * ab.x) + (ab.y * ab.y)

    if (dotProduct <= 0) return a
    else if(dotProduct >= abLengthSquared) return b

    val projectionX = dotProduct / abLengthSquared * ab.x
    val projectionY = dotProduct / abLengthSquared * ab.y

    val cX = a.latitude + projectionX
    val cY = a.longitude + projectionY

    return LatLng(cX, cY)
}