package com.example.wearosapp.dependencies

import android.content.Context
import com.example.wearosapp.ui.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, CommunicationModule::class])
interface ApplicationComponent {
    val context: Context
    fun inject(mainActivity: MainActivity)
}