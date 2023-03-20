package com.example.wearosapp.dependencies

import android.content.Context
import androidx.room.Room
import com.example.wearosapp.database.ApplicationDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): ApplicationDatabase {
        return Room.databaseBuilder(
            context,
            ApplicationDatabase::class.java, DATABASE_NAME
        ).build()
    }

    companion object {
        const val DATABASE_NAME = "measurement.db"
    }
}