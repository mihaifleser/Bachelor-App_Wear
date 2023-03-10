/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.wearosapp.ui.activity

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.wearosapp.sensors.GeneralSensorListener
import com.example.wearosapp.ui.screen.MainScreen

class MainActivity : ComponentActivity() {

    private lateinit var sensorManager: SensorManager
    private val sensorListener = GeneralSensorListener()

    private lateinit var gyroscope: Sensor
    private lateinit var accelerometer: Sensor
    private lateinit var gravity: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            MainScreen(
                navigateGesture = { startActivity(Intent(applicationContext, GestureActivity::class.java)) },
                navigateConnect = { startActivity(Intent(applicationContext, ConnectActivity::class.java)) })
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