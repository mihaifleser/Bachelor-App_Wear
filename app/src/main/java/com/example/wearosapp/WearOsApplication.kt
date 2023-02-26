package com.example.wearosapp

import android.app.Application
import com.example.wearosapp.dependencies.ApplicationComponent
import com.example.wearosapp.dependencies.ApplicationModule
import com.example.wearosapp.dependencies.DaggerApplicationComponent

class WearOsApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        createApplicationComponent()
    }

    private fun createApplicationComponentBuilder(): DaggerApplicationComponent.Builder {
        return DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this))
    }

    private fun createApplicationComponent() {
        applicationComponent = createApplicationComponentBuilder().build()
    }
}