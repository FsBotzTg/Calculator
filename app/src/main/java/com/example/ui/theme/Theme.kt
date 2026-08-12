package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.example.data.ThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = DarkOperatorBtn,
    secondary = DarkEqualsBtn,
    tertiary = DarkActionBtn,
    background = DarkBg,
    surface = DarkSurface,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    onPrimary = Color.White,
    onSecondary = DarkBg
)

private val LightColorScheme = lightColorScheme(
    primary = LightOperatorBtn,
    secondary = LightEqualsBtn,
    tertiary = LightActionBtn,
    background = LightBg,
    surface = LightSurface,
    onBackground = LightTextPrimary,
    onSurface = LightTextPrimary,
    onPrimary = Color.White,
    onSecondary = Color.White
)

val DarkCalcColors = CalculatorColors(
    bg = DarkBg,
    displayBg = DarkSurface,
    numberBtnBg = DarkNumberBtn,
    numberBtnText = DarkTextPrimary,
    actionBtnBg = DarkActionBtn,
    actionBtnText = DarkTextPrimary,
    operatorBtnBg = DarkOperatorBtn,
    operatorBtnText = Color.White,
    equalsBtnBg = DarkEqualsBtn,
    equalsBtnText = DarkBg,
    scientificBtnBg = Color(0xFF1E293B),
    scientificBtnText = Color(0xFFCBD5E1),
    textPrimary = DarkTextPrimary,
    textSecondary = DarkTextSecondary
)

val LightCalcColors = CalculatorColors(
    bg = LightBg,
    displayBg = LightSurface,
    numberBtnBg = LightNumberBtn,
    numberBtnText = LightTextPrimary,
    actionBtnBg = LightActionBtn,
    actionBtnText = LightTextPrimary,
    operatorBtnBg = LightOperatorBtn,
    operatorBtnText = Color.White,
    equalsBtnBg = LightEqualsBtn,
    equalsBtnText = Color.White,
    scientificBtnBg = Color(0xFFE2E8F0),
    scientificBtnText = Color(0xFF334155),
    textPrimary = LightTextPrimary,
    textSecondary = LightTextSecondary
)

@Composable
fun CalculatorAppTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val calcColors = if (darkTheme) DarkCalcColors else LightCalcColors

    CompositionLocalProvider(
        LocalCalculatorColors provides calcColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
