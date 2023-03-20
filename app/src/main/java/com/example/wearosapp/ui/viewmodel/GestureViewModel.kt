package com.example.wearosapp.ui.viewmodel

import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wearosapp.database.Measurement
import com.example.wearosapp.database.MeasurementDao
import kotlinx.coroutines.launch

interface IGestureViewModel {

}

class GestureViewModel(sensorManager: SensorManager, private val measurementDao: MeasurementDao) : SensorViewModel(sensorManager), IGestureViewModel {

    init {
        super.onInit()
        println("Salut")
        insertTest()
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