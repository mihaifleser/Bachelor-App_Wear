package com.example.wearosapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.wearosapp.R
import com.example.wearosapp.ui.theme.WearOsAppTheme
import com.example.wearosapp.ui.viewmodel.IGestureViewModel
import com.example.wearosapp.ui.viewmodel.ScreenState

@Composable
fun GestureScreen(viewModel: IGestureViewModel) {
    WearOsAppTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (viewModel.screenState.value) {
                ScreenState.DATA_COLLECTION -> CollectUserDataScreen(viewModel = viewModel)
                ScreenState.LOADING -> CircularProgressIndicator(modifier = Modifier.padding(5.dp))
                ScreenState.IDLE -> GestureList(viewModel = viewModel)
                ScreenState.START_RECORDING -> Text(text = "Start moving " + viewModel.currentGesture.value)
                ScreenState.RECORDING_IN_PROGRESS -> Text(text = "Recording in progress")
            }
        }
    }
}

@Composable
fun CollectUserDataScreen(viewModel: IGestureViewModel) {
    var name by remember { mutableStateOf("") }
    val listState = rememberScalingLazyListState()
    LaunchedEffect(Unit) {
        listState.scrollToItem(0)
    }
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(
            top = 40.dp,
            start = 10.dp,
            end = 10.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
    )
    {
        item {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                textStyle = TextStyle(color = Color.White),
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.enter_name)) }
            )
        }

        item {
            Button(
                onClick = { viewModel.onNameEntered(name) },
                Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            ) {
                Text(
                    text = stringResource(id = R.string.submit),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun GestureList(viewModel: IGestureViewModel) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 10.dp,
            start = 10.dp,
            end = 10.dp,
            bottom = 40.dp
        ),
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically)
    )
    {
        item {
            Text(text = stringResource(id = R.string.last_gesture, viewModel.lastBatchId.value), textAlign = TextAlign.Center)
        }
        item {
            Text(text = stringResource(id = R.string.choose_gesture, viewModel.currentName.value), textAlign = TextAlign.Center)
        }
        viewModel.gestures.value.map {
            item {
                Button(
                    onClick = it::onClick,
                    Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                ) {
                    Text(
                        text = it.title,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}