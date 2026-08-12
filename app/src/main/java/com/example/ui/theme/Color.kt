package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Core Theme Palette
val DarkBg = Color(0xFF0F172A)          // Slate 900
val DarkSurface = Color(0xFF1E293B)     // Slate 800
val DarkNumberBtn = Color(0xFF334155)   // Slate 700
val DarkActionBtn = Color(0xFF475569)   // Slate 600
val DarkOperatorBtn = Color(0xFF6366F1) // Indigo 500
val DarkEqualsBtn = Color(0xFF38BDF8)   // Sky 400
val DarkTextPrimary = Color(0xFFF8FAFC)
val DarkTextSecondary = Color(0xFF94A3B8)

val LightBg = Color(0xFFF1F5F9)         // Slate 100
val LightSurface = Color(0xFFFFFFFF)    // Pure White
val LightNumberBtn = Color(0xFFE2E8F0)  // Slate 200
val LightActionBtn = Color(0xFFCBD5E1)  // Slate 300
val LightOperatorBtn = Color(0xFF4F46E5)// Indigo 600
val LightEqualsBtn = Color(0xFF0284C7)  // Sky 600
val LightTextPrimary = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF64748B)

@Immutable
data class CalculatorColors(
    val bg: Color,
    val displayBg: Color,
    val numberBtnBg: Color,
    val numberBtnText: Color,
    val actionBtnBg: Color,
    val actionBtnText: Color,
    val operatorBtnBg: Color,
    val operatorBtnText: Color,
    val equalsBtnBg: Color,
    val equalsBtnText: Color,
    val scientificBtnBg: Color,
    val scientificBtnText: Color,
    val textPrimary: Color,
    val textSecondary: Color
)

val LocalCalculatorColors = staticCompositionLocalOf {
    CalculatorColors(
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
        scientificBtnBg = DarkSurface,
        scientificBtnText = DarkTextSecondary,
        textPrimary = DarkTextPrimary,
        textSecondary = DarkTextSecondary
    )
}
