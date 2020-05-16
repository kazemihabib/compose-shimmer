package com.github.kazemihabib.shimmer

import androidx.annotation.FloatRange
import androidx.compose.ambientOf

/**
 * Defines the appearance and the behavior for [shimmer]s.
 *
 * You can define new theme and apply it via [ShimmerThemeAmbient].
 */
data class ShimmerTheme(
    /**
     * Defines the current [ShimmerEffect] implementation.
     */
    val factory: ShimmerEffectFactory,
    /**
     * The alpha for unhighlighted section
     */
    @FloatRange(from = 0.0, to = 1.0)
    val baseAlpha: Float,
    /**
     * The alpha for highlighted section
     */
    @FloatRange(from = 0.0, to = 1.0)
    val highlightAlpha: Float,
    /**
     * The direction of [shimmer] effect
     */
    val direction: Direction,
    /**
     * Controls the size of the fading edge of the highlight.
     */
    @FloatRange(from = 0.0, to = 1.0)
    val dropOff: Float,
    /**
     * Controls the brightness of the highlight at the center
     */
    @FloatRange(from = 0.0, to = 1.0)
    val intensity: Float,
    /**
     * Angle in degrees at which the [shimmer] is tilted
     */
    val tilt: Float
) {
    init {
        require(baseAlpha in 0f..1f) { "baseAlpha should be between 0f and 1f but it's $baseAlpha" }

        require(highlightAlpha in 0f..1f) { "highlightAlpha should be between 0f and 1f but it's $highlightAlpha" }

        require(dropOff in 0f..1f) { "dropOff should be between 0f and 1f but it's $dropOff" }

        require(intensity in 0f..1f) { "intensity should be between 0f and 1f but it's $intensity" }
    }
}

/**
 *  Enum class defining the direction of [shimmer] effect
 */
enum class Direction {
    LeftToRight,
    TopToBottom,
    RightToLeft,
    BottomToTop
}

/**
 * Ambient used for providing [ShimmerTheme] down the tree.
 */
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
