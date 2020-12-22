package com.github.kazemihabib.compose_shimmer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import com.github.kazemihabib.shimmer.*


class ShimmerModel {

    var durationMs: Int by mutableStateOf(3000)

    var delay: Int by mutableStateOf(0)

    var baseAlpha: Float by mutableStateOf(0.3f)

    var highlightAlpha: Float by mutableStateOf(0.9f)

    var direction: ShimmerDirection by mutableStateOf(ShimmerDirection.LeftToRight)

    var dropOff: Float by mutableStateOf(0.5f)

    var intensity: Float by mutableStateOf(0f)

    var tilt: Float by mutableStateOf(20f)

    var repeatMode: RepeatMode by mutableStateOf(RepeatMode.RESTART)

}

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
        val model by remember { mutableStateOf(ShimmerModel()) }
        Providers(
            ShimmerThemeProvider provides ShimmerTheme(
                factory = DefaultLinearShimmerEffectFactory,
                baseAlpha = model.baseAlpha,
                highlightAlpha = model.highlightAlpha,
                direction = model.direction,
                dropOff = model.dropOff,
                intensity = model.intensity,
                tilt = model.tilt
            )
        ) {
            ScrollableColumn(modifier = Modifier.padding(20.dp)) {
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
private fun RadioGroup(
    options: List<String>,
    selectedOption: String?,
    onSelectedChange: (String) -> Unit
) {
    Column {
        options.forEach {
            Row {
                Text(text = it)
                Spacer(modifier = Modifier.width(8.dp))
                RadioButton(selected = selectedOption == it, onClick = { onSelectedChange(it) })
            }
        }
    }
}


@Composable
fun ShimmerSample(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        repeat(3) {
            PlaceHolder()
            Spacer(modifier = Modifier.height(20.dp))

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
            .background(color = Color.LightGray)
    )
}

@Composable
fun ImagePlaceHolder() {
    Box(modifier = Modifier.size(110.dp).background(color = Color.LightGray))
}


@Composable
private fun Config(model: ShimmerModel) {

    val repeatModes = mapOf(RepeatMode.RESTART to "Restart", RepeatMode.REVERSE to "Reverse")

    Log.d("MOVL", "model:$model")
    LabelRadio(
        label = "repeat",
        map = repeatModes,
        selectedKey = model.repeatMode,
        onSelectedChange = model::repeatMode::set
    )
    val directions = mapOf<ShimmerDirection, String>(
        ShimmerDirection.LeftToRight to "left to right",
        ShimmerDirection.RightToLeft to "right to left",
        ShimmerDirection.TopToBottom to "top to bottom",
        ShimmerDirection.BottomToTop to "bottom to top"
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


