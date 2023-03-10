package com.example.wearosapp

import android.app.Application
import com.example.wearosapp.dependencies.ApplicationComponent
import com.example.wearosapp.dependencies.ApplicationModule
import com.example.wearosapp.dependencies.DaggerApplicationComponent

class WearOsApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    }

}