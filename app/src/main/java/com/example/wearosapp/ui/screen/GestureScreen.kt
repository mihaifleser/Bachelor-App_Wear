package com.example.wearosapp.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import com.example.wearosapp.R
import com.example.wearosapp.ui.theme.WearOsAppTheme

@Composable
fun GestureScreen() {
    WearOsAppTheme {
        ScalingLazyColumn {
            item {
                Text(text = stringResource(id = R.string.choose_gesture), textAlign = TextAlign.Center)
            }
        }
    }
}