package com.bogdan801.digitalfarmer.data.util

import android.util.Size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.bogdan801.digitalfarmer.R
import com.google.android.gms.maps.MapView
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

fun getBoundsZoomLevel(bounds: LatLngBounds, mapDim: Size): Float {
    val worldDim = Size(256, 256)
    val zoomMax = 21.toDouble();

    fun latRad(lat: Double): Double {
        val sin = sin(lat * Math.PI / 180);
        val radX2 = ln((1 + sin) / (1 - sin)) / 2;
        return max(min(radX2, Math.PI), -Math.PI) /2
    }

    fun zoom(mapPx: Int, worldPx: Int, fraction: Double): Double {
        return floor(Math.log(mapPx / worldPx / fraction) / Math.log(2.0))
    }

    val ne = bounds.northeast;
    val sw = bounds.southwest;

    val latFraction = (latRad(ne.latitude) - latRad(sw.latitude)) / Math.PI;

    val lngDiff = ne.longitude - sw.longitude;
    val lngFraction = if (lngDiff < 0) { (lngDiff + 360) / 360 } else { (lngDiff / 360) }

    val latZoom = zoom(mapDim.height, worldDim.height, latFraction);
    val lngZoom = zoom(mapDim.width, worldDim.width, lngFraction);

    return minOf(latZoom, lngZoom, zoomMax).toFloat()
}


/*
@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}
*/

/*
@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }*/

/*
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
}*/
