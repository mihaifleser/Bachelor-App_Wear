package com.example.wearosapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.wearosapp.R
import com.example.wearosapp.ui.theme.WearOsAppTheme
import com.example.wearosapp.ui.viewmodel.IConnectViewModel

@Composable
fun ConnectScreen(viewModel: IConnectViewModel) {
    WearOsAppTheme {
        val loading by viewModel.loading.observeAsState(false)
        val phoneName by viewModel.phoneName.observeAsState("")
        val searchVisible by viewModel.searchVisible.observeAsState(false)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (searchVisible) {
                Button(
                    onClick = viewModel::startSearching,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.connect))
                }
            } else {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary,
                    text = "Connected to\n$phoneName"
                )
                Button(
                    onClick = viewModel::sendTest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Text(text = "Send Test")
                }
            }
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.padding(5.dp))
            }
        }
    }
}
