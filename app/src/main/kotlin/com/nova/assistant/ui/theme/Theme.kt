package com.nova.assistant.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Nova Color Scheme - Dark Futuristic Theme
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF58A6FF),           // Nova Blue
    onPrimary = Color(0xFF0D1117),         // Dark background for text
    primaryContainer = Color(0xFF1C2128),   // Darker blue container
    onPrimaryContainer = Color(0xFF58A6FF), // Nova Blue text
    
    secondary = Color(0xFF7C3AED),          // Purple accent
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF2D1B69), // Dark purple container
    onSecondaryContainer = Color(0xFF7C3AED),
    
    tertiary = Color(0xFFFFB347),           // Warm orange
    onTertiary = Color(0xFF0D1117),
    tertiaryContainer = Color(0xFF3D2914),  // Dark orange container
    onTertiaryContainer = Color(0xFFFFB347),
    
    error = Color(0xFFFF6B6B),             // Nova Red
    onError = Color.White,
    errorContainer = Color(0xFF4D1F1F),    // Dark red container
    onErrorContainer = Color(0xFFFF6B6B),
    
    background = Color(0xFF0D1117),         // GitHub Dark background
    onBackground = Color(0xFFF0F6FC),       // Light text on dark
    
    surface = Color(0xFF161B22),            // Slightly lighter surface
    onSurface = Color(0xFFF0F6FC),          // Light text
    surfaceVariant = Color(0xFF21262D),     // Card/component surface
    onSurfaceVariant = Color(0xFFB1BAC4),   // Muted text
    
    outline = Color(0xFF30363D),            // Borders and dividers
    outlineVariant = Color(0xFF21262D),     // Subtle outlines
    
    scrim = Color(0x80000000),              // Overlay scrim
    
    inverseSurface = Color(0xFFF0F6FC),     // Light surface for contrast
    inverseOnSurface = Color(0xFF0D1117),   // Dark text on light
    inversePrimary = Color(0xFF0969DA),     // Darker blue for light backgrounds
    
    surfaceDim = Color(0xFF0D1117),         // Dimmed surface
    surfaceBright = Color(0xFF30363D),      // Bright surface variant
    surfaceContainerLowest = Color(0xFF0D1117),
    surfaceContainerLow = Color(0xFF161B22),
    surfaceContainer = Color(0xFF21262D),
    surfaceContainerHigh = Color(0xFF30363D),
    surfaceContainerHighest = Color(0xFF484F58)
)

// Light color scheme (for completeness, though app focuses on dark theme)
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0969DA),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDDF4FF),
    onPrimaryContainer = Color(0xFF0550AE),
    
    secondary = Color(0xFF7C3AED),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF3E8FF),
    onSecondaryContainer = Color(0xFF5B21B6),
    
    tertiary = Color(0xFFD97706),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFEF3C7),
    onTertiaryContainer = Color(0xFF92400E),
    
    error = Color(0xFFDC2626),
    onError = Color.White,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF991B1B),
    
    background = Color(0xFFFAFBFC),
    onBackground = Color(0xFF24292F),
    
    surface = Color.White,
    onSurface = Color(0xFF24292F),
    surfaceVariant = Color(0xFFF6F8FA),
    onSurfaceVariant = Color(0xFF656D76),
    
    outline = Color(0xFFD0D7DE),
    outlineVariant = Color(0xFFF6F8FA)
)

@Composable
fun NovaAssistantTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to maintain consistent Nova branding
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
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}