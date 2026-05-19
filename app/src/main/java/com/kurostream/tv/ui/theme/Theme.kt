package com.kurostream.tv.ui.theme

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
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = androidx.tv.material3.dark.PaletteKeyColorTokens.Primary,
    secondary = androidx.tv.material3.dark.PaletteKeyColorTokens.Secondary,
    tertiary = androidx.tv.material3.dark.PaletteKeyColorTokens.Tertiary,
    background = androidx.tv.material3.dark.PaletteKeyColorTokens.Background,
    surface = androidx.tv.material3.dark.PaletteKeyColorTokens.Surface,
    onPrimary = androidx.tv.material3.dark.PaletteKeyColorTokens.OnPrimary,
    onSecondary = androidx.tv.material3.dark.PaletteKeyColorTokens.OnSecondary,
    onTertiary = androidx.tv.material3.dark.PaletteKeyColorTokens.OnTertiary,
    onBackground = androidx.tv.material3.dark.PaletteKeyColorTokens.OnBackground,
    onSurface = androidx.tv.material3.dark.PaletteKeyColorTokens.OnSurface,
)

private val LightColorScheme = lightColorScheme(
    primary = androidx.tv.material3.light.PaletteKeyColorTokens.Primary,
    secondary = androidx.tv.material3.light.PaletteKeyColorTokens.Secondary,
    tertiary = androidx.tv.material3.light.PaletteKeyColorTokens.Tertiary,
    background = androidx.tv.material3.light.PaletteKeyColorTokens.Background,
    surface = androidx.tv.material3.light.PaletteKeyColorTokens.Surface,
    onPrimary = androidx.tv.material3.light.PaletteKeyColorTokens.OnPrimary,
    onSecondary = androidx.tv.material3.light.PaletteKeyColorTokens.OnSecondary,
    onTertiary = androidx.tv.material3.light.PaletteKeyColorTokens.OnTertiary,
    onBackground = androidx.tv.material3.light.PaletteKeyColorTokens.OnBackground,
    onSurface = androidx.tv.material3.light.PaletteKeyColorTokens.OnSurface,
)

@Composable
fun KuroStreamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

val Typography = androidx.compose.material3.Typography(
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = androidx.compose.ui.text.font.FontFamily.Default,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
        fontSize = androidx.compose.ui.unit.sp
    )
)
