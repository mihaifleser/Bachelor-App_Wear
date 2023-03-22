package com.example.wearosapp.ui.viewmodel

import com.example.wearosapp.database.MeasurementType

interface IGestureItemViewModel {
    val title: String
    fun onClick()
}

class GestureItemViewModel(private val measurementType: MeasurementType, private val onClick: (measurementType: MeasurementType) -> Unit) :
    IGestureItemViewModel {

    override val title: String
        get() = measurementType.description

    override fun onClick() {
        onClick.invoke(measurementType)
    }
}