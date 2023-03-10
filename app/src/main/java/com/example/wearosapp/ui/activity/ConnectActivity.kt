package com.example.wearosapp.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.wearosapp.WearOsApplication
import com.example.wearosapp.ui.screen.ConnectScreen
import com.example.wearosapp.ui.viewmodel.ConnectViewModel
import com.example.wearosapp.ui.viewmodel.IConnectViewModel
import com.example.wearosapp.ui.viewmodel.MainViewModelFactory
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.MessageClient
import javax.inject.Inject

class ConnectActivity : ComponentActivity() {

    @Inject
    internal lateinit var capabilityClient: CapabilityClient

    @Inject
    internal lateinit var messageClient: MessageClient

    private val viewModel: IConnectViewModel by lazy {
        val factory = MainViewModelFactory(capabilityClient, messageClient)
        ViewModelProvider(this, factory)[ConnectViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as WearOsApplication).applicationComponent.inject(this)

        setContent {
            ConnectScreen(viewModel = viewModel)
        }
    }
}