package com.example.wearosapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import com.example.wearosapp.ui.theme.WearOsAppTheme
import com.example.wearosapp.ui.viewmodel.NeuralViewModel
import com.example.wearosapp.ui.viewmodel.ScreenState

@Composable
fun NeuralScreen(viewModel: NeuralViewModel) {
    WearOsAppTheme {
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            viewModel
                .toastMessage
                .collect { message ->
                    Toast.makeText(
                        context,
                        message,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (viewModel.screenState.value) {
                ScreenState.IDLE -> Text(text = "Idle State")
                ScreenState.LOADING -> CircularProgressIndicator(modifier = Modifier.padding(5.dp))
                ScreenState.RECORDING_IN_PROGRESS -> Text(text = "Recording in progress")
                ScreenState.START_RECORDING -> Text(text = "Watch Idle, Start Moving")
                ScreenState.DATA_COLLECTION -> Text(text = "Data Collection State")
            }
        }
    }
}