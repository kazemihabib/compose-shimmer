package com.github.kazemihabib.shimmer

import androidx.animation.AnimationClockObservable

interface ShimmerEffectFactory {

    fun create(
        baseAlpha: Float,

        highlightAlpha: Float,

        direction: Direction,

        dropOff: Float,

        intensity: Float,

        tilt: Float,

        durationMs: Int = 3000,

        delay: Int,

        repeatMode: RepeatMode = RepeatMode.RESTART,

        clock: AnimationClockObservable
    ): ShimmerEffect

}
