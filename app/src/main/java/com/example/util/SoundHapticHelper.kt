package com.example.util

import android.view.HapticFeedbackConstants
import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.ui.hapticfeedback.HapticFeedback

object SoundHapticHelper {

    fun performClickFeedback(
        view: View?,
        hapticFeedback: HapticFeedback?,
        hapticsEnabled: Boolean,
        soundEnabled: Boolean
    ) {
        if (hapticsEnabled && view != null) {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        }
        if (soundEnabled && view != null) {
            view.playSoundEffect(SoundEffectConstants.CLICK)
        }
    }
}
