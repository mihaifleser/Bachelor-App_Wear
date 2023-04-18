package com.example.wearosapp.ui.viewmodel

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import com.example.wearosapp.database.Measurement
import kotlin.math.abs

open class SensorViewModel(private val sensorManager: SensorManager) : SensorEventListener, ViewModel() {

    protected var xRotation: Float = 0f
    protected var yRotation: Float = 0f
    protected var zRotation: Float = 0f

    protected var xAcceleration: Float = 0f
    protected var yAcceleration: Float = 0f
    protected var zAcceleration: Float = 0f

    protected var allMeasurements: MutableList<Measurement> = emptyList<Measurement>().toMutableList()

    protected fun onInit() {
        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        println(deviceSensors)

        val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL, GESTURE_DURATION_MCS / SAMPLES)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, GESTURE_DURATION_MCS / SAMPLES)
    }

    override fun onCleared() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        when (p0?.sensor?.type) {
            Sensor.TYPE_GYROSCOPE -> {
                xRotation = p0.values?.get(0)!!
                yRotation = p0.values?.get(1)!!
                zRotation = p0.values?.get(2)!!
                //println("Rotation: $xRotation $yRotation $zRotation")
            }
            Sensor.TYPE_ACCELEROMETER -> {
                xAcceleration = p0.values?.get(0)!!
                yAcceleration = p0.values?.get(1)!!
                zAcceleration = p0.values?.get(2)!!
                //println("Acceleration: $xAcceleration $yAcceleration $zAcceleration")
                if (allMeasurements.size > SAMPLES) {
                    allMeasurements.removeAll(allMeasurements.subList(0, allMeasurements.size - SAMPLES).toList())
                }
                allMeasurements.add(Measurement(0, 0, xRotation, yRotation, zRotation, xAcceleration, yAcceleration, zAcceleration))
            }
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        println("Accuracy changed")
    }

    protected fun isMoving(): Boolean {
        return abs(xAcceleration) > LIMIT || abs(yAcceleration) > LIMIT || abs(zAcceleration - GRAVITY) > LIMIT
    }

    companion object {
        const val GESTURE_DURATION_MCS = 1000000 // 1 second
        const val PREVIOUS_SAMPLES = 15
        const val SAMPLES = 100
        const val GRAVITY = 9.8
        const val LIMIT = 3.2
    }
}