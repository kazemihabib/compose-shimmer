package com.github.kazemihabib.shimmer

import androidx.annotation.FloatRange
import androidx.compose.ambientOf

data class ShimmerTheme(
    val factory: ShimmerEffectFactory,

    @FloatRange(from = 0.0, to = 1.0)
    val baseAlpha: Float,

    @FloatRange(from = 0.0, to = 1.0)
    val highlightAlpha: Float,

    val direction: Direction,

    @FloatRange(from = 0.0, to = 1.0)
    val dropOff: Float,

    @FloatRange(from = 0.0, to = 1.0)
    val intensity: Float,

    val tilt: Float
) {
    init {
        require(baseAlpha in 0f..1f) { "baseAlpha should be between 0f and 1f but it's $baseAlpha" }

        require(highlightAlpha in 0f..1f) { "highlightAlpha should be between 0f and 1f but it's $highlightAlpha" }

        require(dropOff in 0f..1f) { "dropOff should be between 0f and 1f but it's $dropOff" }

        require(intensity in 0f..1f) { "intensity should be between 0f and 1f but it's $intensity" }
    }
}

enum class Direction {
    LeftToRight,
    TopToBottom,
    RightToLeft,
    BottomToTop
}

val ShimmerThemeAmbient = ambientOf { defaultShimmerTheme }

private val defaultShimmerTheme = ShimmerTheme(
    factory = DefaultLinearShimmerEffectFactory,
    baseAlpha = 0.2f,
    highlightAlpha = 0.9f,
    direction = Direction.TopToBottom,
    dropOff = 0.5f,
    intensity = 0f,
    tilt = 0f
)
