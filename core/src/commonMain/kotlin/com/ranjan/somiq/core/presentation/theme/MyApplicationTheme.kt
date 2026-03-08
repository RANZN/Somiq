package com.ranjan.somiq.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = SomiqGold,
            onPrimary = Color.Black,
            primaryContainer = Color(0xFF2D2A36),
            secondary = SomiqLavender,
            tertiary = SomiqCoral,
            surface = Color(0xFF1C1B1F),
            surfaceVariant = Color(0xFF2D2A36),
        )
    } else {
        lightColorScheme(
            primary = SomiqGold,
            onPrimary = Color.Black,
            primaryContainer = Color(0xFFFFF4D6),
            secondary = SomiqLavender,
            tertiary = SomiqCoral,
            surface = Color.White,
            surfaceVariant = SomiqSurfaceVariantSoft,
            outlineVariant = Color(0xFFE8E5ED),
        )
    }
    // Font resources should be provided by the app module
    // For now, using default font family
    val fontFamily = FontFamily.Default
    val defaultTypography = Typography()
    val typography = Typography(
        displayLarge = defaultTypography.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = defaultTypography.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = defaultTypography.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = defaultTypography.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = defaultTypography.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = fontFamily),
    )
    val shapes = Shapes(
        small = RoundedCornerShape(12.dp),
        medium = RoundedCornerShape(20.dp),
        large = RoundedCornerShape(28.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content,
    )
}
