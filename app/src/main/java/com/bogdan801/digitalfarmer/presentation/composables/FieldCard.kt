package com.bogdan801.digitalfarmer.presentation.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogdan801.digitalfarmer.data.util.toFormattedDateString
import com.bogdan801.digitalfarmer.domain.model.Field

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FieldCard(
    modifier: Modifier = Modifier,
    field: Field,
    widthRatio: Double = 5.0,
    heightRatio: Double = 4.0,
    isExpanded: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onMapStartedLoading: () -> Unit = {},
    onMapFinishedLoading: () -> Unit = {}
) {
    val density = LocalDensity.current
    val backgroundColor by animateColorAsState(
        targetValue = if(!isSelected) MaterialTheme.colorScheme.surfaceVariant
                      else MaterialTheme.colorScheme.surface,
        label = ""
    )
    Card(
        modifier = modifier
            .combinedClickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick
            ),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = if(!isSelected) null else BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
    ) {
        var globalWidth by remember { mutableStateOf(0.dp) }
        var collapsedCardWidth by remember { mutableStateOf(0.dp) }
        var collapsedCardHeight by remember { mutableStateOf(0.dp) }
        val expandedSpacerHeight = ((heightRatio * collapsedCardWidth.value)/widthRatio).dp
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coords ->
                    globalWidth = with(density) { coords.size.width.toDp() }
                }
        ) {
            val spacerHeight by animateDpAsState(
                targetValue = if (isExpanded) expandedSpacerHeight else 0.dp,
                label = "puk"
            )
            Column(
                modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(spacerHeight))
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .onGloballyPositioned { coords ->
                            collapsedCardWidth = with(density) { coords.size.width.toDp() } + 32.dp
                            collapsedCardHeight =
                                with(density) { coords.size.height.toDp() } + 32.dp
                        }
                ) {
                    Text(text = field.name, fontSize = 24.sp)
                    Text(
                        text = "${collapsedCardWidth.value} ${collapsedCardHeight.value}"
                    )
                    Text(text = field.plantDate?.toFormattedDateString() ?: "Not planted yet")
                    Text(text = field.harvestDate?.toFormattedDateString() ?: "Not planted yet")
                }
            }

            if(collapsedCardWidth > 0.dp && collapsedCardHeight > 0.dp){
                val snapshotWidth by animateDpAsState(
                    targetValue = if(isExpanded) collapsedCardWidth
                    else ((widthRatio * collapsedCardHeight.value)/heightRatio).dp,
                    label = "puk"
                )
                val snapshotHeight by animateDpAsState(
                    targetValue = if(isExpanded) expandedSpacerHeight
                    else collapsedCardHeight,
                    label = "puk"
                )
                FieldSnapshot(
                    modifier = Modifier
                        .size(
                            width = snapshotWidth,
                            height = snapshotHeight
                        )
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(12.dp)),
                    field = field,
                    shouldSaveSnapshot = isExpanded,
                    onMapStartedLoading = onMapStartedLoading,
                    onSnapshotGenerated = { onMapFinishedLoading() }
                )
            }
        }
    }
}