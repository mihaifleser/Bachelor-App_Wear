package com.example.wearosapp.ui.viewmodel

import android.hardware.SensorManager
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wearosapp.database.Batch
import com.example.wearosapp.database.BatchDao
import com.example.wearosapp.database.Measurement
import com.example.wearosapp.database.MeasurementDao
import com.example.wearosapp.database.MeasurementType
import kotlinx.coroutines.launch

interface IGestureViewModel {
    val gestures: MutableState<List<IGestureItemViewModel>>
    val lastBatchId: MutableState<Int>
    val currentName: MutableState<String>
    val currentGesture: MutableState<String>
    val screenState: MutableState<ScreenState>
    fun onNameEntered(name: String)
}

class GestureViewModel(sensorManager: SensorManager, private val measurementDao: MeasurementDao, private val batchDao: BatchDao) :
    SensorViewModel(sensorManager), IGestureViewModel {

    override val gestures: MutableState<List<IGestureItemViewModel>> by lazy { mutableStateOf(emptyList()) }

    override val lastBatchId: MutableState<Int> by lazy { mutableIntStateOf(0) }

    override val currentName: MutableState<String> by lazy { mutableStateOf("") }

    override val currentGesture: MutableState<String> by lazy { mutableStateOf("") }

    private var currentBatch: Long? = null

    private var name: String = ""
        set(value) {
            field = value
            currentName.value = value
        }

    private val onClick: (measurementType: MeasurementType) -> Unit = {
        screenState.value = ScreenState.LOADING
        println(it.description)
        viewModelScope.launch {
            currentBatch = batchDao.insert(Batch(0, it.type, name))
            println("Current batch: $currentBatch")
            currentGesture.value = it.description
            screenState.value = ScreenState.START_RECORDING
        }
    }

    init {
        super.onInit()
        screenState.value = ScreenState.DATA_COLLECTION
        println("Gesture Screen")
        gestures.value = (MeasurementType.values().map { GestureItemViewModel(it, onClick) })
        updateLastBatchId()
        //insertTest()
    }

    override fun onNameEntered(name: String) {
        this.name = name
        screenState.value = ScreenState.IDLE
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
            updateLastBatchId()
            screenState.value = ScreenState.IDLE
            println("Values inserted!")
        }
    }

    private fun updateLastBatchId() {
        viewModelScope.launch {
            lastBatchId.value = batchDao.getAll().lastOrNull()?.id?.toInt() ?: 0
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