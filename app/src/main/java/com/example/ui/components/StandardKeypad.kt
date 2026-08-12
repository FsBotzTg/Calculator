package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StandardKeypad(
    hapticsEnabled: Boolean,
    soundEnabled: Boolean,
    onInput: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonHeight = 64.dp

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Row 1: AC, ⌫, %, ÷
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                text = "AC",
                type = ButtonType.ACTION,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("AC") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "⌫",
                type = ButtonType.ACTION,
                fontSize = 20.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("⌫") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "%",
                type = ButtonType.ACTION,
                fontSize = 22.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("%") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "÷",
                type = ButtonType.OPERATOR,
                fontSize = 26.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("÷") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
        }

        // Row 2: 7, 8, 9, ×
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                text = "7",
                type = ButtonType.NUMBER,
                fontSize = 24.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("7") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "8",
                type = ButtonType.NUMBER,
                fontSize = 24.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("8") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "9",
                type = ButtonType.NUMBER,
                fontSize = 24.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("9") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "×",
                type = ButtonType.OPERATOR,
                fontSize = 26.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("×") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
        }

        // Row 3: 4, 5, 6, −
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                text = "4",
                type = ButtonType.NUMBER,
                fontSize = 24.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("4") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "5",
                type = ButtonType.NUMBER,
                fontSize = 24.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("5") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "6",
                type = ButtonType.NUMBER,
                fontSize = 24.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("6") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "−",
                type = ButtonType.OPERATOR,
                fontSize = 26.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("−") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
        }

        // Row 4: 1, 2, 3, +
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                text = "1",
                type = ButtonType.NUMBER,
                fontSize = 24.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("1") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "2",
                type = ButtonType.NUMBER,
                fontSize = 24.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("2") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "3",
                type = ButtonType.NUMBER,
                fontSize = 24.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("3") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "+",
                type = ButtonType.OPERATOR,
                fontSize = 26.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("+") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
        }

        // Row 5: ±, 0, ., =
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                text = "±",
                type = ButtonType.NUMBER,
                fontSize = 22.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("±") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "0",
                type = ButtonType.NUMBER,
                fontSize = 24.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("0") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = ".",
                type = ButtonType.NUMBER,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput(".") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "=",
                type = ButtonType.EQUALS,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("=") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
        }
    }
}
