package com.veltrix.tv.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator

object FocusHighlightHelper {

    private const val SCALE_FOCUSED = 1.05f
    private const val SCALE_NORMAL = 1.0f
    private const val ANIMATION_DURATION = 150L

    fun setupFocusHighlight(view: View) {
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.setOnFocusChangeListener { v, hasFocus ->
            animateScale(v, hasFocus)
            v.isSelected = hasFocus
        }
    }

    private fun animateScale(view: View, focused: Boolean) {
        val targetScale = if (focused) SCALE_FOCUSED else SCALE_NORMAL
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", targetScale)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", targetScale)
        val elevation = ObjectAnimator.ofFloat(
            view, "translationZ",
            if (focused) 8f else 0f
        )
        AnimatorSet().apply {
            playTogether(scaleX, scaleY, elevation)
            duration = ANIMATION_DURATION
            interpolator = DecelerateInterpolator()
            start()
        }
    }

    fun applyFocusState(view: View, hasFocus: Boolean) {
        val targetScale = if (hasFocus) SCALE_FOCUSED else SCALE_NORMAL
        view.scaleX = targetScale
        view.scaleY = targetScale
        view.translationZ = if (hasFocus) 8f else 0f
        view.isSelected = hasFocus
    }
}
