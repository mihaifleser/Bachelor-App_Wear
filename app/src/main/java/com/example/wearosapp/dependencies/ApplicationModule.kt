package com.example.wearosapp.dependencies

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return context.applicationContext
    }
}