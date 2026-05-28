package com.yourcompany.tiltballmaze.game.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.max
import kotlin.math.min

class TiltSensor(
    context: Context,
    private val onTiltChanged: (ax: Float, ay: Float) -> Unit
) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val gravitySensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)

    fun start() {
        gravitySensor?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val gx = event.values[0] // влево/вправо
        val gy = event.values[1] // вперёд/назад

        // Нормализуем и инвертируем под удобное управление
        val ax = -gx / 9.81f
        val ay = gy / 9.81f

        onTiltChanged(
            max(-1f, min(1f, ax)),
            max(-1f, min(1f, ay))
        )
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}