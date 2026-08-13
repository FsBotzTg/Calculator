package com.example.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.DisplaySection
import com.example.ui.components.ScientificKeypad
import com.example.ui.components.StandardKeypad
import com.example.ui.history.HistoryBottomSheet
import com.example.ui.settings.SettingsDialog
import com.example.ui.theme.LocalCalculatorColors

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = LocalCalculatorColors.current

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(colors.bg),
        containerColor = colors.bg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 6.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Action Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App Brand
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(colors.operatorBtnBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "Calculator",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                }

                // Mode Toggle Pill & Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Scientific Toggle Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (state.isScientific) colors.operatorBtnBg else colors.actionBtnBg)
                            .clickable { viewModel.toggleScientificMode() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("btn_scientific_toggle"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Functions,
                                contentDescription = "Scientific mode",
                                tint = if (state.isScientific) Color.White else colors.textPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (state.isScientific) "Scientific" else "Standard",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (state.isScientific) Color.White else colors.textPrimary
                            )
                        }
                    }

                    // History Button
                    IconButton(
                        onClick = { viewModel.setHistoryOpen(true) },
                        modifier = Modifier.testTag("btn_history")
                    ) {
                        if (state.history.isNotEmpty()) {
                            BadgedBox(
                                badge = {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(colors.equalsBtnBg)
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = "History",
                                    tint = colors.textPrimary
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "History",
                                tint = colors.textPrimary
                            )
                        }
                    }

                    // Settings Button
                    IconButton(
                        onClick = { viewModel.setSettingsOpen(true) },
                        modifier = Modifier.testTag("btn_settings")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = colors.textPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Display Section
            DisplaySection(
                expression = state.expression,
                resultPreview = state.resultPreview,
                isEvaluated = state.isEvaluated,
                errorMessage = state.errorMessage,
                angleUnit = state.angleUnit,
                isScientific = state.isScientific,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Spacer(modifier = Modifier.height(if (state.isScientific) 6.dp else 12.dp))

            // Scientific Keypad (Expandable)
            AnimatedVisibility(
                visible = state.isScientific,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    ScientificKeypad(
                        angleUnit = state.angleUnit,
                        hapticsEnabled = state.hapticsEnabled,
                        soundEnabled = state.soundEnabled,
                        onInput = { viewModel.onInput(it) }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }

            // Standard Keypad Grid
            StandardKeypad(
                hapticsEnabled = state.hapticsEnabled,
                soundEnabled = state.soundEnabled,
                onInput = { viewModel.onInput(it) },
                isScientific = state.isScientific
            )
        }
    }

    // History Bottom Sheet
    if (state.isHistoryOpen) {
        HistoryBottomSheet(
            historyList = state.history,
            onSelectHistoryItem = { viewModel.selectHistoryItem(it) },
            onDeleteHistoryItem = { viewModel.deleteHistoryItem(it) },
            onClearAllHistory = { viewModel.clearAllHistory() },
            onDismiss = { viewModel.setHistoryOpen(false) }
        )
    }

    // Settings Sheet
    if (state.isSettingsOpen) {
        SettingsDialog(
            themeMode = state.themeMode,
            hapticsEnabled = state.hapticsEnabled,
            soundEnabled = state.soundEnabled,
            numberFormattingEnabled = state.numberFormattingEnabled,
            angleUnit = state.angleUnit,
            onThemeModeChange = { viewModel.updateThemeMode(it) },
            onHapticsChange = { viewModel.updateHapticsEnabled(it) },
            onSoundChange = { viewModel.updateSoundEnabled(it) },
            onFormattingChange = { viewModel.updateFormattingEnabled(it) },
            onAngleUnitChange = { viewModel.toggleAngleUnit() },
            onClearHistory = { viewModel.clearAllHistory() },
            onDismiss = { viewModel.setSettingsOpen(false) }
        )
    }
}
