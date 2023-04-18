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
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.example.wearosapp.R
import com.example.wearosapp.ui.theme.WearOsAppTheme

@Composable
fun MainScreen(actions: List<Pair<Int, () -> Unit>>) {
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
                Text(text = stringResource(id = R.string.choose_action), textAlign = TextAlign.Center)
            }
            actions.forEach {
                item {
                    Button(
                        onClick = it.second,
                        Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = it.first),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}