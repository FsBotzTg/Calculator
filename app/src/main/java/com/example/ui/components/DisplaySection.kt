package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.engine.AngleUnit
import com.example.ui.theme.LocalCalculatorColors

@Composable
fun DisplaySection(
    expression: String,
    resultPreview: String,
    isEvaluated: Boolean,
    errorMessage: String?,
    angleUnit: AngleUnit,
    isScientific: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = LocalCalculatorColors.current
    val scrollState = rememberScrollState()

    // Auto scroll expression display to end as user types
    LaunchedEffect(expression) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    val vertPadding = if (isScientific) 12.dp else 20.dp
    val exprFontSize = if (isScientific) {
        if (isEvaluated) 20.sp else 28.sp
    } else {
        if (isEvaluated) 28.sp else 38.sp
    }
    val resultFontSize = if (isScientific) {
        if (isEvaluated) 32.sp else 20.sp
    } else {
        if (isEvaluated) 44.sp else 24.sp
    }
    val spacerHeight = if (isScientific) 4.dp else 10.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(colors.displayBg)
            .padding(horizontal = 20.dp, vertical = vertPadding)
            .testTag("calculator_display")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Angle Unit Indicator / Mode Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isScientific) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(colors.actionBtnBg.copy(alpha = 0.5f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = angleUnit.name,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textSecondary
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(1.dp))
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacerHeight))

            // Current Expression
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (expression.isEmpty()) "0" else expression,
                    color = if (isEvaluated) colors.textSecondary else colors.textPrimary,
                    fontSize = exprFontSize,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Default,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier.testTag("expression_text")
                )
            }

            Spacer(modifier = Modifier.height(spacerHeight))

            // Result Preview / Final Evaluated Result
            AnimatedContent(
                targetState = when {
                    isEvaluated -> resultPreview
                    resultPreview.isNotEmpty() -> "= $resultPreview"
                    else -> ""
                },
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "resultTransition"
            ) { targetText ->
                Text(
                    text = targetText,
                    color = if (isEvaluated) colors.textPrimary else colors.textSecondary,
                    fontSize = resultFontSize,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("result_text")
                )
            }
        }
    }
}
