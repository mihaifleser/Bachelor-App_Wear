package com.example.wearosapp.ui.viewmodel

import android.hardware.SensorManager
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.wearosapp.database.*
import kotlinx.coroutines.launch

interface IGestureViewModel {
    val gestures: MutableState<List<IGestureItemViewModel>>
    val screenState: MutableState<ScreenState>
}

class GestureViewModel(sensorManager: SensorManager, private val measurementDao: MeasurementDao, private val batchDao: BatchDao) :
    SensorViewModel(sensorManager), IGestureViewModel {

    override val gestures: MutableState<List<IGestureItemViewModel>> by lazy { mutableStateOf(emptyList()) }

    private var currentBatch: Long? = null

    private val onClick: (measurementType: MeasurementType) -> Unit = {
        screenState.value = ScreenState.LOADING
        println(it.description)
        viewModelScope.launch {
            currentBatch = batchDao.insert(Batch(0, it.type))
            println("Current batch: $currentBatch")
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
            measurementDao.insert(Measurement(0, 1, 2.3f, 2.2f, 1f, 2.1f, 3.2f, 11.1f))
            println("Test inserted!")
        }
    }

    override fun onSamplesCollected() {
        println("Samples reached!")
        println(localMeasurements)
        screenState.value = ScreenState.LOADING
        viewModelScope.launch {
            measurementDao.insertAll(*localMeasurements.map { Measurement(it, currentBatch!!) }.toTypedArray())
            localMeasurements.clear()
            screenState.value = ScreenState.IDLE
            println("Values inserted!")
        }
    }
}

class GestureViewModelFactory(private val sensorManager: SensorManager, private val measurementDao: MeasurementDao, private val batchDao: BatchDao) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GestureViewModel::class.java)) {
            return GestureViewModel(sensorManager, measurementDao, batchDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}