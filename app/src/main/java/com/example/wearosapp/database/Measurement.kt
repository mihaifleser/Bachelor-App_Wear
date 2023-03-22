package com.example.wearosapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Measurement(
    @PrimaryKey(autoGenerate = true) val id: Int,

    val type: Int,
    val batch: Int,
    val xRotation: Float,
    val yRotation: Float,
    val zRotation: Float,
    val xAcceleration: Float,
    val yAcceleration: Float,
    val zAcceleration: Float,
)

enum class MeasurementType(val description: String, val type: Int) {
    UP_DOWN("Up and down", 1),
    LEFT_RIGHT("Left and right", 2)
}