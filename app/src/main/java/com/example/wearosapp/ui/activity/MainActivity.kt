/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.wearosapp.ui.activity

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.wearosapp.WearOsApplication
import com.example.wearosapp.sensors.GeneralSensorListener
import com.example.wearosapp.ui.screen.WearApp
import com.example.wearosapp.ui.viewmodel.IMainViewModel
import com.example.wearosapp.ui.viewmodel.MainViewModel
import com.example.wearosapp.ui.viewmodel.MainViewModelFactory
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.MessageClient
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    internal lateinit var capabilityClient: CapabilityClient

    @Inject
    internal lateinit var messageClient: MessageClient

    private val viewModel: IMainViewModel by lazy {
        val factory = MainViewModelFactory(capabilityClient, messageClient)
        ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private lateinit var sensorManager: SensorManager
    private val sensorListener = GeneralSensorListener()

    private lateinit var gyroscope: Sensor
    private lateinit var accelerometer: Sensor
    private lateinit var gravity: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as WearOsApplication).applicationComponent.inject(this)

        super.onCreate(savedInstanceState)

        setContent {
            WearApp(viewModel)
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        println(deviceSensors)

        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorListener)
    }

    private fun registerListeners() {
        sensorManager.registerListener(sensorListener, gyroscope, SensorManager.SENSOR_DELAY_NORMAL, DELAY)
        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, DELAY)
        sensorManager.registerListener(sensorListener, gravity, SensorManager.SENSOR_DELAY_NORMAL, DELAY)
    }

    companion object {
        const val DELAY = 1000
    }

}