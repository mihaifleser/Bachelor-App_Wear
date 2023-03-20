package com.example.wearosapp.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import com.example.wearosapp.R
import com.example.wearosapp.ui.theme.WearOsAppTheme
import com.example.wearosapp.ui.viewmodel.IGestureViewModel

@Composable
fun GestureScreen(viewModel: IGestureViewModel) {
    WearOsAppTheme {
        ScalingLazyColumn {
            item {
                Text(text = stringResource(id = R.string.choose_gesture), textAlign = TextAlign.Center)
            }
        }
    }
}