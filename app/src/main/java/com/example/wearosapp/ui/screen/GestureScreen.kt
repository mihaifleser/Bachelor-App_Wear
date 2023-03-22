package com.example.wearosapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.example.wearosapp.R
import com.example.wearosapp.ui.theme.WearOsAppTheme
import com.example.wearosapp.ui.viewmodel.IGestureViewModel

@Composable
fun GestureScreen(viewModel: IGestureViewModel) {
    WearOsAppTheme {
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
                Text(text = stringResource(id = R.string.choose_gesture), textAlign = TextAlign.Center)
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
}