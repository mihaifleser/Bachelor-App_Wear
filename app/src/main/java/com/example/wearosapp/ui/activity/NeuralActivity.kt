package com.example.wearosapp.ui.activity

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.wearosapp.WearOsApplication
import com.example.wearosapp.ui.screen.NeuralScreen
import com.example.wearosapp.ui.viewmodel.NeuralViewModel
import com.example.wearosapp.ui.viewmodel.NeuralViewModelFactory
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.channels.FileChannel
import javax.inject.Inject

class NeuralActivity : ComponentActivity() {

    @Inject
    lateinit var sensorManager: SensorManager

    lateinit var neuralModel: Interpreter

    private val viewModel: NeuralViewModel by lazy {
        val factory = NeuralViewModelFactory(sensorManager, neuralModel)
        ViewModelProvider(this, factory)[NeuralViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as WearOsApplication).applicationComponent.inject(this)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        neuralModel = getNeuralModel(applicationContext)

        setContent {
            NeuralScreen(viewModel)
        }
    }

    private fun getNeuralModel(context: Context): Interpreter {
        val fileDescriptor = context.assets.openFd("model_100_6.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val mappedByteBuffer = inputStream.channel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
        return Interpreter(mappedByteBuffer, Interpreter.Options())
    }

}