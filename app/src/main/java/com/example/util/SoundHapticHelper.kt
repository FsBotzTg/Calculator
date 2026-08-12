package com.example.util

import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

object SoundHapticHelper {

    fun performClickFeedback(
        view: View?,
        hapticFeedback: HapticFeedback?,
        hapticsEnabled: Boolean,
        soundEnabled: Boolean
    ) {
        if (hapticsEnabled) {
            hapticFeedback?.performHapticFeedback(HapticFeedbackType.KeyTick)
        }
        if (soundEnabled && view != null) {
            view.playSoundEffect(SoundEffectConstants.CLICK)
        }
    }
}
