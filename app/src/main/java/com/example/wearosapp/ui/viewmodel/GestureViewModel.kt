package com.example.wearosapp.ui.viewmodel

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.wearosapp.database.Measurement
import com.example.wearosapp.database.MeasurementDao
import com.example.wearosapp.database.MeasurementType
import kotlinx.coroutines.launch

interface IGestureViewModel {
    val gestures: MutableState<List<IGestureItemViewModel>>
    val screenState: MutableState<ScreenState>
}

class GestureViewModel(sensorManager: SensorManager, private val measurementDao: MeasurementDao) : SensorViewModel(sensorManager), IGestureViewModel {

    override val gestures: MutableState<List<IGestureItemViewModel>> by lazy { mutableStateOf(emptyList()) }

    override val screenState: MutableState<ScreenState> by lazy { mutableStateOf(ScreenState.CHOOSE) }

    private var localMeasurements: MutableList<Measurement> = emptyList<Measurement>().toMutableList()

    private var moving = false

    private lateinit var currentType: MeasurementType

    private var currentBatch: Int? = null

    private val onClick: (measurementType: MeasurementType) -> Unit = {
        screenState.value = ScreenState.LOADING
        println(it.description)
        viewModelScope.launch {
            currentBatch = measurementDao.getLatestBatch() + BATCH_OFFSET
            println("Current batch: $currentBatch")
            currentType = it
            screenState.value = ScreenState.START_RECORDING
        }
    }

    init {
        super.onInit()
        println("Gesture Screen")
        gestures.value = (MeasurementType.values().map { GestureItemViewModel(it, onClick) })
        //insertTest()
    }

    private fun insertTest() {
        viewModelScope.launch {
            measurementDao.insert(Measurement(0, 0, 1, 2.3f, 2.2f, 1f, 2.1f, 3.2f, 11.1f))
            println("Test inserted!")
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        super.onSensorChanged(p0)
        if (p0?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {

            if (screenState.value == ScreenState.START_RECORDING && isMoving()) {
                println("IT is moving!")
                moving = true
                screenState.value = ScreenState.RECORDING_IN_PROGRESS
                localMeasurements.addAll(allMeasurements.subList(allMeasurements.size - PREVIOUS_SAMPLES, allMeasurements.size)
                    .map { Measurement(it, currentType.type, currentBatch!!) })
            }

            if (moving) {
                localMeasurements.add(
                    Measurement(
                        0,
                        currentType.type,
                        currentBatch!!,
                        xRotation,
                        yRotation,
                        zRotation,
                        xAcceleration,
                        yAcceleration,
                        zAcceleration
                    )
                )
                if (localMeasurements.size == SAMPLES) {
                    moving = false
                    println(localMeasurements)
                    screenState.value = ScreenState.LOADING
                    viewModelScope.launch {
                        measurementDao.insertAll(*localMeasurements.toTypedArray())
                        localMeasurements.clear()
                        screenState.value = ScreenState.CHOOSE
                    }
                }
            }
        }
    }

    companion object {
        const val BATCH_OFFSET = 1
    }

}

enum class ScreenState {
    LOADING, CHOOSE, START_RECORDING, RECORDING_IN_PROGRESS
}

class GestureViewModelFactory(private val sensorManager: SensorManager, private val measurementDao: MeasurementDao) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GestureViewModel::class.java)) {
            return GestureViewModel(sensorManager, measurementDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}