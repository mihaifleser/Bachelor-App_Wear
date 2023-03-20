package com.example.wearosapp.dependencies

import android.content.Context
import com.example.wearosapp.ui.activity.ConnectActivity
import com.example.wearosapp.ui.activity.GestureActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, CommunicationModule::class, SensorModule::class, DatabaseModule::class])
interface ApplicationComponent {
    val context: Context
    fun inject(mainActivity: ConnectActivity)
    fun inject(gestureActivity: GestureActivity)
}