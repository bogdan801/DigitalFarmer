package com.bogdan801.digitalfarmer.presentation.composables

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.bogdan801.digitalfarmer.data.util.readBitmapFromPrivateStorage
import com.bogdan801.digitalfarmer.data.util.saveBitmapToPrivateStorage
import com.bogdan801.digitalfarmer.data.util.toFormattedDateString
import com.bogdan801.digitalfarmer.domain.model.Field

@Composable
fun FieldCard(
    modifier: Modifier = Modifier,
    field: Field,
    onDeleteClickButton: () -> Unit = {},
    onEditClickButton: () -> Unit = {}
) {
    val context = LocalContext.current
    var isPreviewSaved by remember { mutableStateOf(readBitmapFromPrivateStorage(context, field.id) != null) }
    Card(modifier = modifier) {
        if(!isPreviewSaved){
            ShapeSnapshotCreator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = field.shape,
                zoom = 15f,
                onSnapShotGenerated = { snapshot ->
                    saveBitmapToPrivateStorage(context, snapshot, field.id)
                    isPreviewSaved = true
                }
            )
        }
        else {
            val preview = remember { mutableStateOf(readBitmapFromPrivateStorage(context, field.id)) }
            preview.value?.let {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Preview of the field",
                    contentScale = ContentScale.Crop
                )
            }
            DisposableEffect(key1 = preview.value){
                onDispose {
                    preview.value?.recycle()
                }
            }
        }

        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    context.deleteFile(field.id)
                    isPreviewSaved = false
                }
            ) {
                Text(text = "Delete file")
            }
            Text(text = field.name, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Source: " + if(!isPreviewSaved) "Map" else "Saved image")
            Text(text = field.plantedCrop.toString())
            Text(text = field.plantDate?.toFormattedDateString() ?: "Not planted yet")
            Text(text = field.harvestDate?.toFormattedDateString() ?: "Not planted yet")
        }
    }
}