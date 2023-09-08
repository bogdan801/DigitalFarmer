package com.bogdan801.digitalfarmer.presentation.composables

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker

@Composable
fun MapMarker(
    state: MarkerState,
    title: String = "",
    draggable: Boolean = false,
    @DrawableRes iconResourceId: Int? = null,
    tint: Color = Color.Black,
    scale: Float = 1f,
    anchor: Offset = Offset(0.5f, 0.5f),
    onClick: (Marker) -> Boolean = {_->false}
) {
    val context = LocalContext.current
    val icon = if(iconResourceId != null) bitmapDescriptorFromVector(context, iconResourceId, scale, tint) else null
    Marker(
        state = state,
        title = title,
        icon = icon,
        anchor = anchor,
        draggable = draggable,
        onClick = onClick
    )
}

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int,
    scale: Float = 3f,
    color: Color = Color.Blue
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, (drawable.intrinsicWidth*scale).toInt(), (drawable.intrinsicHeight*scale).toInt())
    val mutated = DrawableCompat.wrap(drawable).mutate()
    DrawableCompat.setTint(mutated, color.toArgb())
    val bm = Bitmap.createBitmap(
        (drawable.intrinsicWidth*scale).toInt(),
        (drawable.intrinsicHeight*scale).toInt(),
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    mutated.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}