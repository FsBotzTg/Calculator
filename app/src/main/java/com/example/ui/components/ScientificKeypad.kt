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
import com.example.calculator.engine.AngleUnit

@Composable
fun ScientificKeypad(
    angleUnit: AngleUnit,
    hapticsEnabled: Boolean,
    soundEnabled: Boolean,
    onInput: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonHeight = 46.dp

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Row 1: RAD/DEG, sin, cos, tan
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                text = angleUnit.name,
                type = ButtonType.SCIENTIFIC,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput(angleUnit.name) },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "sin",
                type = ButtonType.SCIENTIFIC,
                fontSize = 15.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("sin") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "cos",
                type = ButtonType.SCIENTIFIC,
                fontSize = 15.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("cos") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "tan",
                type = ButtonType.SCIENTIFIC,
                fontSize = 15.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("tan") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
        }

        // Row 2: sin⁻¹, cos⁻¹, tan⁻¹, ln
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                text = "sin⁻¹",
                type = ButtonType.SCIENTIFIC,
                fontSize = 13.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("sin⁻¹") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "cos⁻¹",
                type = ButtonType.SCIENTIFIC,
                fontSize = 13.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("cos⁻¹") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "tan⁻¹",
                type = ButtonType.SCIENTIFIC,
                fontSize = 13.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("tan⁻¹") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "ln",
                type = ButtonType.SCIENTIFIC,
                fontSize = 15.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("ln") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
        }

        // Row 3: log, √, x², xʸ
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                text = "log",
                type = ButtonType.SCIENTIFIC,
                fontSize = 15.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("log") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "√",
                type = ButtonType.SCIENTIFIC,
                fontSize = 17.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("√") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "x²",
                type = ButtonType.SCIENTIFIC,
                fontSize = 15.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("x²") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "xʸ",
                type = ButtonType.SCIENTIFIC,
                fontSize = 15.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("^") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
        }

        // Row 4: π, e, x!, 1/x
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                text = "π",
                type = ButtonType.SCIENTIFIC,
                fontSize = 17.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("π") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "e",
                type = ButtonType.SCIENTIFIC,
                fontSize = 17.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("e") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "x!",
                type = ButtonType.SCIENTIFIC,
                fontSize = 15.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("!") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = "1/x",
                type = ButtonType.SCIENTIFIC,
                fontSize = 15.sp,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("1/x") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
        }

        // Row 5: (, )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                text = "(",
                type = ButtonType.SCIENTIFIC,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput("(") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
            CalculatorButton(
                text = ")",
                type = ButtonType.SCIENTIFIC,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                hapticsEnabled = hapticsEnabled,
                soundEnabled = soundEnabled,
                onClick = { onInput(")") },
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )
        }
    }
}
