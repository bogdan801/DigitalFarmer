package com.bogdan801.digitalfarmer.presentation.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bogdan801.digitalfarmer.domain.model.Crop

@Composable
fun CropSelector(
    modifier: Modifier = Modifier,
    cropsTypes: List<Crop> = Crop.values().asList(),
    inactiveIndexes: List<Int> = listOf(),
    selectedCropIndex: Int = 0,
    onCropSelected: (Int) -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        cropsTypes.forEachIndexed{ id, crop ->
            val buttonSize by animateDpAsState(targetValue = if(id == selectedCropIndex) 50.dp else 40.dp,
                label = ""
            )
            CropButton(
                modifier = Modifier
                    .size(buttonSize)
                    .clickable {
                        onCropSelected(id)
                    },
                iconID = crop.drawableID,
            )
        }
    }
}

@Composable
fun CropButton(
    modifier: Modifier = Modifier,
    iconID: Int
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(id = iconID),
                contentDescription = "Crop Icon"
            )
        }
    }
}