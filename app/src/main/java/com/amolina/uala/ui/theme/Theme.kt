package com.amolina.uala.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val LightColorScheme = lightColorScheme(
    primary = BlueHeader,                // Headers
    secondary = Pink40,            // List rows
    tertiary = OrangeSelectedRow,        // Selected row
    background = Color(0xFFFFFFFF),      // White background
    surface = Color(0xFFFFFFFF),         // White cards
    onPrimary = Color.White,             // Text/icons on header
    onSecondary = Color(0xFF1C1B1F),     // Text/icons on list rows
    onTertiary = Color.White,            // Text/icons on selected row
    onBackground = Color(0xFF1C1B1F),    // Text on white background
    onSurface = Color(0xFF1C1B1F)        // Text on white cards
)

private val DarkColorScheme = darkColorScheme(
    primary = BlueHeader,
    secondary = Pink40,
    tertiary = OrangeSelectedRow,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    onPrimary = Color.White,
    onSecondary = Color(0xFFE5E5EA),
    onTertiary = Color(0xFFE5E5EA),
    onBackground = Color(0xFFE5E5EA),
    onSurface = Color(0xFFE5E5EA)
)

@Composable
fun UalaTheme(
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
