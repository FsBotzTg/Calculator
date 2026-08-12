package com.example.ui.calculator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.engine.AngleUnit
import com.example.calculator.engine.CalculatorEngine
import com.example.calculator.engine.EvalResult
import com.example.data.AppDatabase
import com.example.data.CalculationEntity
import com.example.data.CalculationRepository
import com.example.data.ThemeMode
import com.example.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val calculationRepository = CalculationRepository(db.calculationDao())
    private val userPreferencesRepository = UserPreferencesRepository(application)

    private val _uiState = MutableStateFlow(CalculatorState())
    val uiState: StateFlow<CalculatorState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                calculationRepository.history,
                userPreferencesRepository.preferences
            ) { historyList, prefs ->
                _uiState.update { state ->
                    state.copy(
                        history = historyList,
                        themeMode = prefs.themeMode,
                        hapticsEnabled = prefs.hapticsEnabled,
                        soundEnabled = prefs.soundEnabled,
                        numberFormattingEnabled = prefs.numberFormattingEnabled,
                        angleUnit = prefs.angleUnit,
                        isScientific = prefs.scientificModeExpanded
                    )
                }
            }.collectLatest { }
        }
    }

    fun onInput(symbol: String) {
        when (symbol) {
            "AC" -> clearAll()
            "⌫" -> backspace()
            "=" -> evaluateFinal()
            "±" -> togglePlusMinus()
            "RAD", "DEG" -> toggleAngleUnit()
            else -> appendSymbol(symbol)
        }
    }

    private fun clearAll() {
        _uiState.update {
            it.copy(
                expression = "",
                resultPreview = "",
                isEvaluated = false,
                errorMessage = null
            )
        }
    }

    private fun backspace() {
        val currentState = _uiState.value
        if (currentState.isEvaluated) {
            clearAll()
            return
        }

        var expr = currentState.expression
        if (expr.isEmpty()) return

        // Check for multi-character functions at end of string
        val functions = listOf("sin⁻¹(", "cos⁻¹(", "tan⁻¹(", "sin(", "cos(", "tan(", "log(", "ln(", "√(")
        var match: String? = null
        for (func in functions) {
            if (expr.endsWith(func)) {
                match = func
                break
            }
        }

        expr = if (match != null) {
            expr.dropLast(match.length)
        } else {
            expr.dropLast(1)
        }

        _uiState.update { it.copy(expression = expr, errorMessage = null) }
        updatePreview(expr)
    }

    private fun appendSymbol(symbol: String) {
        val currentState = _uiState.value
        var expr = currentState.expression
        val isEvaluated = currentState.isEvaluated

        // Operators
        val isOperator = symbol in listOf("+", "−", "×", "÷", "^")

        if (isEvaluated) {
            if (isOperator) {
                // Chain calculation using previous result
                expr = if (currentState.resultPreview.isNotEmpty() && currentState.errorMessage == null) {
                    currentState.resultPreview + " " + symbol + " "
                } else {
                    symbol + " "
                }
            } else {
                // New expression
                expr = symbol
            }
            _uiState.update { it.copy(isEvaluated = false, errorMessage = null) }
        } else {
            expr = when (symbol) {
                "+", "−", "×", "÷", "^" -> {
                    if (expr.endsWith(" ")) {
                        expr.dropLast(3) + " " + symbol + " "
                    } else if (expr.isNotEmpty()) {
                        "$expr $symbol "
                    } else if (symbol == "−") {
                        "−"
                    } else {
                        expr
                    }
                }
                "sin", "cos", "tan", "sin⁻¹", "cos⁻¹", "tan⁻¹", "log", "ln", "√" -> {
                    if (expr.isNotEmpty() && !expr.endsWith(" ") && !expr.endsWith("(")) {
                        "$expr × $symbol("
                    } else {
                        "$expr$symbol("
                    }
                }
                "x²" -> {
                    if (expr.isNotEmpty()) "$expr^2" else expr
                }
                "1/x" -> {
                    if (expr.isNotEmpty()) "1 ÷ ($expr)" else "1 ÷ ("
                }
                else -> {
                    expr + symbol
                }
            }
        }

        _uiState.update { it.copy(expression = expr, errorMessage = null) }
        updatePreview(expr)
    }

    private fun togglePlusMinus() {
        val currentState = _uiState.value
        var expr = currentState.expression.trim()
        if (expr.isEmpty()) {
            expr = "−"
        } else if (expr.startsWith("−(")) {
            expr = expr.removePrefix("−(").removeSuffix(")")
        } else if (expr.startsWith("−")) {
            expr = expr.removePrefix("−")
        } else {
            expr = "−($expr)"
        }
        _uiState.update { it.copy(expression = expr, isEvaluated = false, errorMessage = null) }
        updatePreview(expr)
    }

    private fun updatePreview(expr: String) {
        if (expr.isBlank()) {
            _uiState.update { it.copy(resultPreview = "", errorMessage = null) }
            return
        }

        val result = CalculatorEngine.evaluate(
            expression = expr,
            angleUnit = _uiState.value.angleUnit,
            formatNumbers = _uiState.value.numberFormattingEnabled
        )

        when (result) {
            is EvalResult.Success -> {
                _uiState.update { it.copy(resultPreview = result.formatted, errorMessage = null) }
            }
            is EvalResult.Error -> {
                // Don't show preview error while typing incomplete expressions
                _uiState.update { it.copy(resultPreview = "") }
            }
        }
    }

    private fun evaluateFinal() {
        val currentState = _uiState.value
        val expr = currentState.expression.trim()

        if (expr.isBlank()) return

        val result = CalculatorEngine.evaluate(
            expression = expr,
            angleUnit = currentState.angleUnit,
            formatNumbers = currentState.numberFormattingEnabled
        )

        when (result) {
            is EvalResult.Success -> {
                _uiState.update {
                    it.copy(
                        resultPreview = result.formatted,
                        isEvaluated = true,
                        errorMessage = null
                    )
                }
                // Save calculation history
                viewModelScope.launch {
                    calculationRepository.saveCalculation(
                        expression = expr,
                        result = result.formatted,
                        isScientific = currentState.isScientific
                    )
                }
            }
            is EvalResult.Error -> {
                _uiState.update {
                    it.copy(
                        errorMessage = result.message,
                        isEvaluated = false
                    )
                }
            }
        }
    }

    fun toggleScientificMode() {
        val newMode = !_uiState.value.isScientific
        _uiState.update { it.copy(isScientific = newMode) }
        viewModelScope.launch {
            userPreferencesRepository.updateScientificExpanded(newMode)
        }
    }

    fun toggleAngleUnit() {
        val nextUnit = if (_uiState.value.angleUnit == AngleUnit.DEG) AngleUnit.RAD else AngleUnit.DEG
        _uiState.update { it.copy(angleUnit = nextUnit) }
        viewModelScope.launch {
            userPreferencesRepository.updateAngleUnit(nextUnit)
        }
        updatePreview(_uiState.value.expression)
    }

    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            userPreferencesRepository.updateThemeMode(mode)
        }
    }

    fun updateHapticsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateHapticsEnabled(enabled)
        }
    }

    fun updateSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateSoundEnabled(enabled)
        }
    }

    fun updateFormattingEnabled(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateNumberFormattingEnabled(enabled)
        }
        updatePreview(_uiState.value.expression)
    }

    fun selectHistoryItem(item: CalculationEntity) {
        _uiState.update {
            it.copy(
                expression = item.result,
                resultPreview = item.result,
                isEvaluated = true,
                errorMessage = null,
                isHistoryOpen = false
            )
        }
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            calculationRepository.deleteById(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            calculationRepository.clearAll()
        }
    }

    fun setHistoryOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isHistoryOpen = isOpen) }
    }

    fun setSettingsOpen(isOpen: Boolean) {
        _uiState.update { it.copy(isSettingsOpen = isOpen) }
    }
}
