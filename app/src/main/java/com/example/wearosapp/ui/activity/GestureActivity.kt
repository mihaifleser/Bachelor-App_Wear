package com.example.wearosapp.ui.activity

import android.hardware.SensorManager
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.wearosapp.WearOsApplication
import com.example.wearosapp.database.ApplicationDatabase
import com.example.wearosapp.ui.screen.GestureScreen
import com.example.wearosapp.ui.viewmodel.GestureViewModel
import com.example.wearosapp.ui.viewmodel.GestureViewModelFactory
import javax.inject.Inject

class GestureActivity : ComponentActivity() {

    @Inject
    lateinit var sensorManager: SensorManager

    @Inject
    lateinit var applicationDatabase: ApplicationDatabase

    private val viewModel: GestureViewModel by lazy {
        val factory = GestureViewModelFactory(sensorManager, applicationDatabase.measurementDao(), applicationDatabase.batchDao())
        ViewModelProvider(this, factory)[GestureViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as WearOsApplication).applicationComponent.inject(this)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            GestureScreen(viewModel)
        }
    }

}