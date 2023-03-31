package com.example.wearosapp.database

import androidx.room.*

@Dao
interface BatchDao {
    @Insert
    suspend fun insertAll(vararg batches: Batch)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(batch: Batch): Long

    @Delete
    suspend fun delete(batch: Batch)

    @Query("SELECT * FROM batch")
    suspend fun getAll(): List<Batch>

}