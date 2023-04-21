package com.example.wearosapp.ui.viewmodel

import android.hardware.SensorManager
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wearosapp.database.MeasurementType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

interface INeuralViewModel {
    val toastMessage: SharedFlow<String>
    val screenState: MutableState<ScreenState>
}

class NeuralViewModel(sensorManager: SensorManager, private val neuralModel: Interpreter) : SensorViewModel(sensorManager), INeuralViewModel {

    override val toastMessage = MutableSharedFlow<String>()

    private val input = ByteBuffer.allocateDirect(100 * 6 * 4).order(ByteOrder.nativeOrder())

    init {
        super.onInit()
        println("Testing neural network")
        screenState.value = ScreenState.START_RECORDING
    }

    override fun onSamplesCollected() {
        screenState.value = ScreenState.LOADING
        println("Hai te pup!")
        localMeasurements.forEach {
            input.putFloat(it.xRotation)
            input.putFloat(it.yRotation)
            input.putFloat(it.zRotation)
            input.putFloat(it.xAcceleration)
            input.putFloat(it.yAcceleration)
            input.putFloat(it.zAcceleration)
        }
        localMeasurements.clear()
        val output = Array(1) { FloatArray(1) }
        neuralModel.run(input, output)
        println("Result: " + output[0][0])
        val gesture = if (output[0][0] < 0.5) MeasurementType.UP_DOWN else MeasurementType.LEFT_RIGHT
        viewModelScope.launch { toastMessage.emit(gesture.description) }
        input.clear()
        screenState.value = ScreenState.START_RECORDING
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