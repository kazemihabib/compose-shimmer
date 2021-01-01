package com.github.kazemihabib.shimmer

import androidx.compose.animation.asDisposableClock
import androidx.compose.animation.core.AnimationClockObservable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import com.github.kazemihabib.shimmer.RepeatMode.RESTART
import com.github.kazemihabib.shimmer.RepeatMode.REVERSE

/**
 * Shimmer is a [Modifier] which adds a shimmering effect to any widget.
 *
 * @sample com.github.kazemihabib.compose_shimmer.App
 *
 * @param durationMs Time it takes for the highlight to move from one end of the layout to the other.
 * @param delay Delay after which the current animation will repeat.
 * @param repeatMode What the animation should do after reaching the end, either restart from the beginning or reverse back towards it.
 * @param clock The animation clock observable that will drive this ripple effect
 */
fun Modifier.shimmer(
    durationMs: Int = 3000,
    delay: Int = 0,
    repeatMode: RepeatMode = RepeatMode.RESTART,
    clock: AnimationClockObservable? = null
): Modifier = composed {
    @Suppress("NAME_SHADOWING") // don't allow usage of the parameter clock, only the disposable
    val clock = (clock ?: AnimationClockAmbient.current).asDisposableClock()
    val theme = ShimmerThemeProvider.current

    val shimmerModifier =
        remember(theme, delay, durationMs, repeatMode, clock) { //TODO("Decrease the reallocation")
            ShimmerModifier(
                shimmerTheme = theme,
                durationMs = durationMs,
                delay = delay,
                repeatMode = repeatMode,
                clock = clock
            )
        }

    shimmerModifier
}

internal class ShimmerModifier(
    val shimmerTheme: ShimmerTheme,
    val durationMs: Int,
    val delay: Int,
    val repeatMode: RepeatMode,
    clock: AnimationClockObservable
) : DrawModifier, LayoutModifier {

    private var size = Size(0f, 0f)
    private val paint = Paint()
    private val factory: ShimmerEffect = shimmerTheme.run {
        factory.create(
            baseAlpha = baseAlpha,
            durationMs = durationMs,
            repeatMode = repeatMode,
            tilt = tilt,
            dropOff = dropOff,
            intensity = intensity,
            highlightAlpha = highlightAlpha,
            direction = direction,
            delay = delay,
            clock = clock
        )
    }

    override fun ContentDrawScope.draw() {
        drawIntoCanvas { canvas ->
            canvas.withSaveLayer(
                Rect(
                    0f,
                    0f,
                    this@ShimmerModifier.size.width,
                    this@ShimmerModifier.size.height
                ), paint
            ) {
                drawContent()
                factory.draw(
                    canvas,
                    this@ShimmerModifier.size
                )
            }
        }
    }


    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        size = Size(width = placeable.width.toFloat(), height = placeable.height.toFloat())
        factory.updateSize(size)
        return layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}

/**
 * [RESTART] restarts the [shimmer] effect animation after reaching the end of layout
 * [REVERSE] reverses the [shimmer] effect animation after reaching the end of layout
 */
enum class RepeatMode {
    RESTART,
    REVERSE
}

