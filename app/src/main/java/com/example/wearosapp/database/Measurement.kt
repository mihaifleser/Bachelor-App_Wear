package com.example.wearosapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Measurement(
    @PrimaryKey(autoGenerate = true) val id: Int,

    var type: Int = 0,
    var batch: Int = 0,
    val xRotation: Float,
    val yRotation: Float,
    val zRotation: Float,
    val xAcceleration: Float,
    val yAcceleration: Float,
    val zAcceleration: Float,
) {
    constructor(measurement: Measurement, type: Int, batch: Int) : this(
        measurement.id,
        type,
        batch,
        measurement.xRotation,
        measurement.yRotation,
        measurement.zRotation,
        measurement.xAcceleration,
        measurement.yAcceleration,
        measurement.zAcceleration
    )
}

enum class MeasurementType(val description: String, val type: Int) {
    UP_DOWN("Up and down", 1),
    LEFT_RIGHT("Left and right", 2)
}