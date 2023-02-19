package com.example.wearosapp.presentation.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class GeneralSensorListener : SensorEventListener {

    var xRotation: Float = 0f
    var yRotation: Float = 0f
    var zRotation: Float = 0f

    var xAcceleration: Float = 0f
    var yAcceleration: Float = 0f
    var zAcceleration: Float = 0f

    var xGravity: Float = 0f
    var yGravity: Float = 0f
    var zGravity: Float = 0f

    override fun onSensorChanged(p0: SensorEvent?) {
        when (p0?.sensor?.type) {
            Sensor.TYPE_GYROSCOPE -> {
                xRotation = p0.values?.get(0)!!
                yRotation = p0.values?.get(1)!!
                zRotation = p0.values?.get(2)!!
                println("Rotation: $xRotation $yRotation $zRotation")
            }
            Sensor.TYPE_ACCELEROMETER -> {
                xAcceleration = p0.values?.get(0)!!
                yAcceleration = p0.values?.get(1)!!
                zAcceleration = p0.values?.get(2)!!
                println("Acceleration: $xAcceleration $yAcceleration $zAcceleration")
            }
            Sensor.TYPE_GRAVITY -> {
                xGravity = p0.values?.get(0)!!
                yGravity = p0.values?.get(1)!!
                zGravity = p0.values?.get(2)!!
                println("Gravity: $xGravity $yGravity $zGravity")
            }
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        println("Accuracy changed")
    }
}