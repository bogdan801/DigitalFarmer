package com.bogdan801.digitalfarmer.presentation.composables

import android.graphics.Bitmap
import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.bogdan801.digitalfarmer.data.util.getBoundsZoomLevel
import com.bogdan801.digitalfarmer.data.util.readBitmapFromPrivateStorage
import com.bogdan801.digitalfarmer.data.util.saveBitmapToPrivateStorage
import com.bogdan801.digitalfarmer.data.util.toFormattedDateString
import com.bogdan801.digitalfarmer.domain.model.Field
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import kotlin.math.roundToInt

@Composable
fun FieldCard(
    modifier: Modifier = Modifier,
    field: Field,
    onDeleteClickButton: () -> Unit = {},
    onEditClickButton: () -> Unit = {}
) {
    val context = LocalContext.current
    var bitmapPreview: Bitmap? by remember { mutableStateOf(readBitmapFromPrivateStorage(context, field.id)) }
    Card(modifier = modifier) {
        if(bitmapPreview == null){
            ShapeSnapshotCreator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = field.shape,
                zoom = 15f,
                onSnapShotGenerated = { snapshot ->
                    saveBitmapToPrivateStorage(context, snapshot, field.id)
                    bitmapPreview = snapshot
                }
            )
        }
        else {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                painter = rememberAsyncImagePainter(bitmapPreview),
                contentDescription = "Preview of the field"
            )
        }

        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    context.deleteFile(field.id)
                    bitmapPreview = readBitmapFromPrivateStorage(context, field.id)
                }
            ) {
                Text(text = "Delete file")
            }
            Text(text = field.name, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Source: " + if(bitmapPreview == null) "Map" else "Saved image")
            Text(text = field.plantedCrop.toString())
            Text(text = field.plantDate?.toFormattedDateString() ?: "Not planted yet")
            Text(text = field.harvestDate?.toFormattedDateString() ?: "Not planted yet")
        }
    }

    DisposableEffect(key1 = bitmapPreview){
        onDispose {
            bitmapPreview?.recycle()
        }
    }
}