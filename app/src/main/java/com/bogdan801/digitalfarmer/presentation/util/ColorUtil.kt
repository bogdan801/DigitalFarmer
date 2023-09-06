package com.bogdan801.digitalfarmer.presentation.util

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

fun containerColor(
    containerColor: Color,
    scrolledContainerColor: Color,
    colorTransitionFraction: Float
): Color {
    return lerp(
        containerColor,
        scrolledContainerColor,
        FastOutLinearInEasing.transform(colorTransitionFraction)
    )
}