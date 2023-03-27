package com.example.wearosapp.database

import androidx.room.*

@Dao
interface MeasurementDao {
    @Insert
    suspend fun insertAll(vararg measurements: Measurement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(measurement: Measurement)

    @Delete
    suspend fun delete(measurement: Measurement)

    @Query("SELECT * FROM measurement")
    suspend fun getAll(): List<Measurement>

    @Query("SELECT COALESCE(MAX(batch), 0) AS max_batch FROM measurement;")
    suspend fun getLatestBatch(): Int

}