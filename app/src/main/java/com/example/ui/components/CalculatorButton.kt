package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalCalculatorColors
import com.example.util.SoundHapticHelper

enum class ButtonType {
    NUMBER, OPERATOR, ACTION, EQUALS, SCIENTIFIC
}

@Composable
fun CalculatorButton(
    text: String,
    type: ButtonType,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 22.sp,
    fontWeight: FontWeight = FontWeight.Medium,
    shapeRadius: Dp = 20.dp,
    hapticsEnabled: Boolean = true,
    soundEnabled: Boolean = false,
    onClick: () -> Unit
) {
    val colors = LocalCalculatorColors.current
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(stiffness = 500f, dampingRatio = 0.6f),
        label = "btnScale"
    )

    val (bgColor, textColor) = when (type) {
        ButtonType.NUMBER -> colors.numberBtnBg to colors.numberBtnText
        ButtonType.OPERATOR -> colors.operatorBtnBg to colors.operatorBtnText
        ButtonType.ACTION -> colors.actionBtnBg to colors.actionBtnText
        ButtonType.EQUALS -> colors.equalsBtnBg to colors.equalsBtnText
        ButtonType.SCIENTIFIC -> colors.scientificBtnBg to colors.scientificBtnText
    }

    val shape = RoundedCornerShape(shapeRadius)

    Box(
        modifier = modifier
            .scale(scale)
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .shadow(
                elevation = if (type == ButtonType.EQUALS || type == ButtonType.OPERATOR) 4.dp else 1.dp,
                shape = shape
            )
            .clip(shape)
            .background(bgColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                SoundHapticHelper.performClickFeedback(view, haptic, hapticsEnabled, soundEnabled)
                onClick()
            }
            .testTag("btn_$text")
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = TextAlign.Center
        )
    }
}
