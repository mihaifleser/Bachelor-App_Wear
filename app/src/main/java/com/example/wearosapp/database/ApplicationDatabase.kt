package com.example.wearosapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Measurement::class, Batch::class], version = 1)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun measurementDao(): MeasurementDao
    abstract fun batchDao(): BatchDao
}