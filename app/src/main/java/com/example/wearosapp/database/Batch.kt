package com.example.wearosapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Batch(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var type: Int = 0,
    var userName: String = "Default"
)