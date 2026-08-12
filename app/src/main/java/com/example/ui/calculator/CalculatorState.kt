package com.example.ui.calculator

import com.example.calculator.engine.AngleUnit
import com.example.data.CalculationEntity
import com.example.data.ThemeMode

data class CalculatorState(
    val expression: String = "",
    val resultPreview: String = "",
    val isEvaluated: Boolean = false,
    val errorMessage: String? = null,
    val isScientific: Boolean = false,
    val angleUnit: AngleUnit = AngleUnit.DEG,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val hapticsEnabled: Boolean = true,
    val soundEnabled: Boolean = false,
    val numberFormattingEnabled: Boolean = true,
    val history: List<CalculationEntity> = emptyList(),
    val isHistoryOpen: Boolean = false,
    val isSettingsOpen: Boolean = false
)
