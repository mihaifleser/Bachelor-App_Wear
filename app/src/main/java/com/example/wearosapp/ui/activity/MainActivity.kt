/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.wearosapp.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.wearosapp.R
import com.example.wearosapp.ui.screen.MainScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val actions: List<Pair<Int, () -> Unit>> = listOf(
            R.string.record_gesture to { startActivity(Intent(applicationContext, GestureActivity::class.java)) },
            R.string.establish_connection to { startActivity(Intent(applicationContext, ConnectActivity::class.java)) },
            R.string.test_neural to { startActivity(Intent(applicationContext, NeuralActivity::class.java)) }
        )

        setContent {
            MainScreen(actions)
        }
    }

}