package com.bogdan801.digitalfarmer.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val DarkColorScheme = darkColorScheme(
    primary = Brown80,
    secondary = DarkBrown80,
    tertiary = Green80,
    error = Red80,
    onPrimary = Brown20,
    onSecondary = DarkBrown20,
    onTertiary = Green20,
    onError = Red20,
    primaryContainer = Brown30,
    secondaryContainer = DarkBrown30,
    tertiaryContainer = Green30,
    errorContainer = Red30,
    onPrimaryContainer = Brown90,
    onSecondaryContainer = DarkBrown90,
    onTertiaryContainer = Green90,
    onErrorContainer = Red90,
    inversePrimary = Brown40,
    surface = Gray6,
    surfaceVariant = Gray24,
    surfaceTint = PaleBrown80,
    onSurface = Gray80,
    onSurfaceVariant = PaleBrown90,
    outline = PaleBrown60,
    outlineVariant = PaleBrown30,
    inverseSurface = Gray80,
    inverseOnSurface = Gray10,
    scrim = Gray0,
    background = Gray6
)

private val LightColorScheme = lightColorScheme(
    primary = Brown40,
    secondary = DarkBrown20,
    tertiary = Green20,
    error = Red40,
    onPrimary = Brown100,
    onSecondary = DarkBrown100,
    onTertiary = Green100,
    onError = Red100,
    primaryContainer = Brown90,
    secondaryContainer = DarkBrown90,
    tertiaryContainer = Green90,
    errorContainer = Red90,
    onPrimaryContainer = Brown10,
    onSecondaryContainer = DarkBrown10,
    onTertiaryContainer = Green10,
    onErrorContainer = Red10,
    inversePrimary = Brown80,
    surface = Gray97,
    surfaceVariant = Gray95,
    surfaceTint = Brown30,
    onSurface = Gray10,
    onSurfaceVariant = PaleBrown30,
    outline = PaleBrown50,
    outlineVariant = PaleBrown80,
    inverseSurface = Gray20,
    inverseOnSurface = Gray95,
    scrim = Gray0,
    background = Gray97
)

@Composable
fun DigitalFarmerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}