package com.github.kazemihabib.compose_shimmer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.Providers
import androidx.compose.remember
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.drawBackground
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.RadioGroup
import androidx.ui.material.Slider
import androidx.ui.material.ripple.ripple
import androidx.ui.unit.dp
import com.github.kazemihabib.shimmer.*
import com.github.kazemihabib.shimmer.Direction

@Model
data class ShimmerModel(

    var durationMs: Int = 3000,

    var delay: Int = 0,

    var baseAlpha: Float = 0.3f,

    var highlightAlpha: Float = 0.9f,

    var direction: Direction = Direction.LeftToRight,

    var dropOff: Float = 0.5f,

    var intensity: Float = 0f,

    var tilt: Float = 20f,

    var repeatMode: RepeatMode = com.github.kazemihabib.shimmer.RepeatMode.RESTART
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}


@Composable
fun App() {
    MaterialTheme {
        val model = remember { ShimmerModel() }
        Providers(
            ShimmerThemeAmbient provides ShimmerTheme(
                factory = DefaultLinearShimmerEffectFactory,
                baseAlpha = model.baseAlpha,
                highlightAlpha = model.highlightAlpha,
                direction = model.direction,
                dropOff = model.dropOff,
                intensity = model.intensity,
                tilt = model.tilt
            )
        ) {
            VerticalScroller {
                Column(modifier = Modifier.padding(20.dp)) {
                    ShimmerSample(
                        modifier = Modifier.shimmer(
                            durationMs = model.durationMs,
                            delay = model.delay,
                            repeatMode = model.repeatMode
                        )
                    )
                    Config(model)
                }
            }
        }

    }
}


@Composable
private fun LabelSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>
) {
    Row {
        Text(label, modifier = Modifier.preferredWidth(100.dp))
        Slider(value = value, onValueChange = onValueChange, valueRange = range)
    }
}

@Composable
private fun <K> LabelRadio(
    label: String,
    map: Map<K, String>,
    selectedKey: K,
    onSelectedChange: (K) -> Unit
) {
    Row {
        Text(label, modifier = Modifier.preferredWidth(100.dp))
        RadioGroup(
            options = map.values.toList(),
            selectedOption = map[selectedKey],
            onSelectedChange = { selected ->
                onSelectedChange(map.filterValues { it == selected }.keys.first())
            })
    }
}


@Composable
fun ShimmerSample(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        repeat(3) {
            PlaceHolder()
            Spacer(modifier = Modifier.height(20.dp))
            Modifier.ripple()
        }
    }
}

@Composable
fun PlaceHolder(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        ImagePlaceHolder()
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            repeat(3) {
                LinePlaceHolder()
                Spacer(modifier = Modifier.height(10.dp))
            }
            LinePlaceHolder(modifier = Modifier.preferredWidth(200.dp))
        }
    }
}

@Composable
fun LinePlaceHolder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().preferredHeight(20.dp)
            .drawBackground(color = Color.LightGray)
    )
}

@Composable
fun ImagePlaceHolder() {
    Box(modifier = Modifier.size(110.dp).drawBackground(color = Color.LightGray))
}


@Composable
private fun Config(model: ShimmerModel) {

    val repeatModes = mapOf(RepeatMode.RESTART to "Restart", RepeatMode.REVERSE to "Reverse")
    LabelRadio(
        label = "repeat",
        map = repeatModes,
        selectedKey = model.repeatMode,
        onSelectedChange = model::repeatMode::set
    )
    val directions = mapOf(
        Direction.LeftToRight to "left to right", Direction.RightToLeft to "right to left",
        Direction.TopToBottom to "top to bottom", Direction.BottomToTop to "bottom to top"
    )

    LabelRadio(
        label = "direction",
        map = directions,
        selectedKey = model.direction,
        onSelectedChange = model::direction::set
    )

    LabelSlider(
        label = "base alpha",
        value = model.baseAlpha,
        onValueChange = model::baseAlpha::set,
        range = 0f..1f
    )
    LabelSlider(
        label = "highlight alpha",
        value = model.highlightAlpha,
        onValueChange = model::highlightAlpha::set,
        range = 0f..1f
    )
    LabelSlider(
        label = "tilt",
        value = model.tilt,
        onValueChange = model::tilt::set,
        range = 0f..90f
    )
    LabelSlider(
        label = "drop off",
        value = model.dropOff,
        onValueChange = model::dropOff::set,
        range = 0f..1f
    )
    LabelSlider(
        label = "intensity",
        value = model.intensity,
        onValueChange = model::intensity::set,
        range = 0f..1f
    )
    LabelSlider(
        label = "duration",
        value = model.durationMs.toFloat(),
        onValueChange = { model.durationMs = it.toInt() },
        range = 1000f..10000f
    )

    LabelSlider(
        label = "delay",
        value = model.delay.toFloat(),
        onValueChange = { model.delay = it.toInt() },
        range = 0f..10000f
    )
}


