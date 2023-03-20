package com.example.wearosapp.dependencies

import android.content.Context
import android.hardware.SensorManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SensorModule {

    @Provides
    @Singleton
    fun provideSensorManager(context: Context): SensorManager {
        return context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
}