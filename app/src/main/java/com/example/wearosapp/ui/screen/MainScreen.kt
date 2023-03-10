package com.example.wearosapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.wearosapp.R
import com.example.wearosapp.ui.theme.WearOsAppTheme

@Composable
fun MainScreen(navigateGesture: () -> Unit, navigateConnect: () -> Unit) {
    WearOsAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = navigateGesture,
                Modifier
                    .padding(5.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.record_gesture),
                    Modifier.padding(start = 10.dp, end = 10.dp)
                )
            }
            Button(
                onClick = navigateConnect
            ) {
                Text(
                    text = stringResource(id = R.string.establish_connection),
                    Modifier
                        .padding(start = 10.dp, end = 10.dp)
                )
            }
        }
    }
}