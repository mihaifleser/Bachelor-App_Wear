package com.example.wearosapp.ui.viewmodel

import android.hardware.SensorManager
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wearosapp.database.MeasurementType
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Node
import kotlinx.coroutines.Dispatchers
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

class NeuralViewModel(sensorManager: SensorManager, private val neuralModel: Interpreter, private val messageClient: MessageClient, private val node: Node?) :
    SensorViewModel(sensorManager),
    INeuralViewModel {

    override val toastMessage = MutableSharedFlow<String>()

    private val input = ByteBuffer.allocateDirect(100 * 6 * 4).order(ByteOrder.nativeOrder())

    init {
        super.onInit()
        println("Testing neural network")
        screenState.value = ScreenState.START_RECORDING
    }

    override fun onSamplesCollected() {
        screenState.value = ScreenState.LOADING
        println("Gesture recorded")
        localMeasurements.forEach {
            input.putFloat(it.xRotation)
            input.putFloat(it.yRotation)
            input.putFloat(it.zRotation)
            input.putFloat(it.xAcceleration)
            input.putFloat(it.yAcceleration)
            input.putFloat(it.zAcceleration)
        }
        localMeasurements.clear()
        val output = Array(1) { FloatArray(MeasurementType.values().size) }
        neuralModel.run(input, output)
        println("Result: " + output[0][0] + " " + output[0][1] + " " + output[0][2])
        val gesture = MeasurementType.fromNeuralResult(output[0])
        gesture?.let { sendMessage(it.name) }
        viewModelScope.launch { toastMessage.emit(gesture?.description ?: "Gesture not recognised") }
        input.clear()
        screenState.value = ScreenState.START_RECORDING
    }

    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            println("Start sending message")
            node?.also { node ->
                messageClient.sendMessage(
                    node.id,
                    ConnectViewModel.SENSOR_PROCESSING,
                    message.toByteArray()
                ).apply {
                    addOnSuccessListener {
                        println("Message sent!")
                    }
                    addOnFailureListener {
                        print("Message Failed")
                    }
                }
            }
        }
    }
}

class NeuralViewModelFactory(
    private val sensorManager: SensorManager,
    private val neuralModel: Interpreter,
    private val messageClient: MessageClient,
    private val node: Node?
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NeuralViewModel::class.java)) {
            return NeuralViewModel(sensorManager, neuralModel, messageClient, node) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}