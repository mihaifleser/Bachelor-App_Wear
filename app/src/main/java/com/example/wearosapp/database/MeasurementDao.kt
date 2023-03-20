package com.example.wearosapp.database

import androidx.room.*

@Dao
interface MeasurementDao {
    @Insert
    fun insertAll(vararg measurements: Measurement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(measurement: Measurement)

    @Delete
    fun delete(measurement: Measurement)

    @Query("SELECT * FROM measurement")
    fun getAll(): List<Measurement>

    @Query("SELECT * FROM measurement WHERE type = :type")
    fun loadAllUsersOlderThan(type: Int): Array<Measurement>

}