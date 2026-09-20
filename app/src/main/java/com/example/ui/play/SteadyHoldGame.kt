package com.example.ui.play

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleySand
import com.example.ui.theme.AbleyTeal
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * A stillness hold measured by the phone's accelerometer.
 *
 * This is the one that keeps the child off the screen rather than on it. The phone is held
 * against the chest, tucked in a pocket or set on the head, and it measures whether the body is
 * steady -- so the activity is still balancing on one leg in the living room, and the screen is
 * only the instrument.
 *
 * Falls back to a plain countdown when there is no accelerometer, which is rare but real on the
 * cheapest devices, and the activity works either way.
 */
@Composable
fun SteadyHoldGame(
    holdSeconds: Int,
    onComplete: (steadyPercent: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    }
    val accelerometer = remember(sensorManager) {
        sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    var wobble by remember { mutableFloatStateOf(0f) }
    var secondsHeld by remember { mutableIntStateOf(0) }
    var steadyTicks by remember { mutableIntStateOf(0) }
    var finished by remember { mutableStateOf(false) }

    DisposableEffect(accelerometer) {
        if (accelerometer == null || sensorManager == null) {
            onDispose { }
        } else {
            var last = FloatArray(3)
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    val d = sqrt(
                        (0..2).sumOf { i ->
                            val diff = (event.values[i] - last[i]).toDouble()
                            diff * diff
                        }
                    ).toFloat()
                    last = event.values.copyOf()
                    // Smoothed so one twitch does not swing the meter across the screen.
                    wobble = wobble * 0.82f + d * 0.18f
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
            }
            sensorManager.registerListener(
                listener,
                accelerometer,
                SensorManager.SENSOR_DELAY_GAME
            )
            onDispose { sensorManager.unregisterListener(listener) }
        }
    }

    LaunchedEffect(holdSeconds) {
        while (secondsHeld < holdSeconds) {
            delay(1000)
            secondsHeld++
            // A generous threshold. Children are not statues and the point is the attempt.
            if (accelerometer == null || wobble < 1.6f) steadyTicks++
        }
        finished = true
        onComplete(if (holdSeconds == 0) 100 else steadyTicks * 100 / holdSeconds)
    }

    val steadiness = (1f - (wobble / 4f)).coerceIn(0f, 1f)

    Column(
        modifier = modifier.fillMaxWidth().testTag("steady_hold_game"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(210.dp)) {
                val centre = Offset(size.width / 2f, size.height / 2f)
                val r = size.minDimension / 2f - 12f
                drawCircle(color = AbleySand, radius = r, center = centre, style = Stroke(width = 12f))
                drawArc(
                    color = if (steadiness > 0.55f) AbleyTeal else AbleyCoral,
                    startAngle = -90f,
                    sweepAngle = 360f * (secondsHeld.toFloat() / holdSeconds.coerceAtLeast(1)),
                    useCenter = false,
                    style = Stroke(width = 12f),
                    topLeft = Offset(centre.x - r, centre.y - r),
                    size = androidx.compose.ui.geometry.Size(r * 2, r * 2)
                )
                // Inner dot drifts with the wobble, so "steady" is something you can see.
                drawCircle(
                    color = if (steadiness > 0.55f) AbleyTeal else AbleyCoral,
                    radius = 16f + (1f - steadiness) * 26f,
                    center = centre
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (finished) "Held it" else "${holdSeconds - secondsHeld}",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = if (finished) {
                        ""
                    } else if (accelerometer == null) {
                        "seconds"
                    } else if (steadiness > 0.55f) {
                        "steady"
                    } else {
                        "wobbling"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black.copy(alpha = 0.55f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (accelerometer == null) {
                "Hold the pose until the ring closes."
            } else {
                "Hold the phone against your chest and keep still until the ring closes."
            },
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black.copy(alpha = 0.5f)
        )
        if (finished) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Steady for ${steadyTicks} of $holdSeconds seconds",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = AbleyTeal
            )
        }
    }
}
