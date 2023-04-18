package com.example.wearosapp.ui.viewmodel

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

interface INeuralViewModel {

}

class NeuralViewModel(sensorManager: SensorManager, private val neuralModel: Interpreter) : SensorViewModel(sensorManager), INeuralViewModel {

    private val input = ByteBuffer.allocateDirect(100 * 6 * 4).order(ByteOrder.nativeOrder())

    private var first: Boolean = true

    init {
        super.onInit()
        println("Testing neural network")
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        super.onSensorChanged(p0)
        if (p0?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            if (allMeasurements.size == SAMPLES && first) {
                println("Hai te pup!")
                first = false
                allMeasurements.forEach {
                    input.putFloat(it.xRotation)
                    input.putFloat(it.yRotation)
                    input.putFloat(it.zRotation)
                    input.putFloat(it.xAcceleration)
                    input.putFloat(it.yAcceleration)
                    input.putFloat(it.zAcceleration)
                }
                val output = Array(1) { FloatArray(1) }
                neuralModel.run(input, output)
                println("Result: " + output[0][0])

            }
        }
    }
}

class NeuralViewModelFactory(private val sensorManager: SensorManager, private val neuralModel: Interpreter) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NeuralViewModel::class.java)) {
            return NeuralViewModel(sensorManager, neuralModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}