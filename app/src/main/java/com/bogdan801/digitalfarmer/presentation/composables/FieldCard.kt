package com.bogdan801.digitalfarmer.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogdan801.digitalfarmer.data.util.openGoogleMapsAtLocation
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
    onEditClick: () -> Unit = {},
    onCalculateClick: () -> Unit = {},
    onMapStartedLoading: () -> Unit = {},
    onMapFinishedLoading: () -> Unit = {}
) {
    val context = LocalContext.current
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
                label = ""
            )
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(spacerHeight))
                val columnPadding = 16.dp
                Column(
                    modifier = Modifier
                        .padding(
                            start = columnPadding,
                            end = columnPadding,
                            top = animateDpAsState(
                                targetValue = if (isExpanded) 8.dp else columnPadding, label = ""
                            ).value,
                            bottom = animateDpAsState(
                                targetValue = if (isExpanded) 0.dp else columnPadding, label = ""
                            ).value,
                        )
                        .fillMaxWidth()
                        .onGloballyPositioned { coords ->
                            collapsedCardWidth =
                                with(density) { coords.size.width.toDp() } + columnPadding * 2
                            collapsedCardHeight =
                                with(density) { coords.size.height.toDp() } + columnPadding * 2
                        }
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = if (!isExpanded) ((widthRatio * collapsedCardHeight.value) / heightRatio).dp else 0.dp),
                        text = field.name,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                append("Area:")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                append(" ${String.format("%.3f", field.shape.area * 0.0001)} ha")
                            }
                        }
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                append("Crop:")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                append(" ${field.plantedCrop ?: "Not planted yet"}")
                            }
                        }
                    )
                }
                AnimatedVisibility(
                    visible = isExpanded
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = columnPadding)
                    ) {
                        val center = field.shape.center
                        Text(
                            modifier = Modifier.clickable {
                                openGoogleMapsAtLocation(context, center)
                            },
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                ) {
                                    append("Coordinates: ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                ) {
                                    append(
                                        String.format("%.6f", center.latitude) + ", " +
                                        String.format("%.6f", center.longitude)
                                    )
                                }
                            }
                        )
                        if(field.plantDate != null) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    ) {
                                        append("Planting date: ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    ) {
                                        append(field.plantDate.toFormattedDateString())
                                    }
                                }
                            )
                        }
                        if(field.harvestDate != null) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    ) {
                                        append("Estimated harvest date: ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    ) {
                                        append(field.harvestDate.toFormattedDateString())
                                    }
                                }
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 8.dp,
                                    bottom = columnPadding
                                ),
                            horizontalArrangement = Arrangement.End
                        ){
                            OutlinedButton(
                                modifier = Modifier.width(120.dp),
                                onClick = onEditClick
                            ) {
                                Text(text = "Edit")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                modifier = Modifier.width(120.dp),
                                onClick = onCalculateClick
                            ) {
                                Text(text = "Calculate")
                            }
                        }
                    }
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