package com.example.wearosapp.ui.viewmodel

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel

open class SensorViewModel(private val sensorManager: SensorManager) : SensorEventListener, ViewModel() {

    protected var xRotation: Float = 0f
    protected var yRotation: Float = 0f
    protected var zRotation: Float = 0f

    protected var xAcceleration: Float = 0f
    protected var yAcceleration: Float = 0f
    protected var zAcceleration: Float = 0f

    protected var xGravity: Float = 0f
    protected var yGravity: Float = 0f
    protected var zGravity: Float = 0f

    protected fun onInit() {
        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        println(deviceSensors)

        val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)

        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL, DELAY)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, DELAY)
        sensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL, DELAY)
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
            }
            Sensor.TYPE_GRAVITY -> {
                xGravity = p0.values?.get(0)!!
                yGravity = p0.values?.get(1)!!
                zGravity = p0.values?.get(2)!!
                //println("Gravity: $xGravity $yGravity $zGravity")
            }
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        println("Accuracy changed")
    }

    companion object {
        const val DELAY = 1000
    }
}