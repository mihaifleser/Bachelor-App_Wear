package com.example.wearosapp

import android.app.Application
import com.example.wearosapp.dependencies.ApplicationComponent
import com.example.wearosapp.dependencies.ApplicationModule
import com.example.wearosapp.dependencies.DaggerApplicationComponent
import com.google.android.gms.wearable.Node

class WearOsApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    var node: Node? = null

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    }

}