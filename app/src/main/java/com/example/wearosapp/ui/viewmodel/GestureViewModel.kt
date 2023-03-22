package com.example.wearosapp.ui.viewmodel

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
}

class GestureViewModel(sensorManager: SensorManager, private val measurementDao: MeasurementDao) : SensorViewModel(sensorManager), IGestureViewModel {

    override val gestures: MutableState<List<IGestureItemViewModel>> by lazy { mutableStateOf(emptyList()) }

    private val onClick: (measurementType: MeasurementType) -> Unit = {
        println(it.description)
    }

    init {
        super.onInit()
        println("Gesture Screen")
        gestures.value = (MeasurementType.values().map { GestureItemViewModel(it, onClick) })
        //insertTest()
    }

    private fun insertTest() {
        viewModelScope.launch {
            measurementDao.insert(Measurement(0, 1, 1, 2.3f, 2.2f, 1f, 2.1f, 3.2f, 11.1f))
            println("Test inserted!")
        }
    }

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