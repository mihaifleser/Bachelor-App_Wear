package com.example.wearosapp.ui.viewmodel

import androidx.lifecycle.*
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Node
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface IConnectViewModel {
    val loading: LiveData<Boolean>
    val phoneName: LiveData<String>
    val searchVisible: LiveData<Boolean>
    val node: LiveData<Node>
    fun startSearching()
    fun sendTest()
}

class ConnectViewModel(private val capabilityClient: CapabilityClient, private val messageClient: MessageClient) : ViewModel(), IConnectViewModel {

    override val loading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    override val phoneName: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    override val searchVisible: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    override val node: MutableLiveData<Node> by lazy { MutableLiveData<Node>() }

    private var processingNode: Node? = null
        set(value) {
            field = value
            node.postValue(value)
            searchVisible.postValue(value == null)
            if (value != null) {
                phoneName.postValue(value.displayName)
            }
        }

    init {
        searchVisible.postValue(true)
    }

    override fun startSearching() {
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            println("Connecting to phone...")
            val capabilityInfo = Tasks.await(
                capabilityClient.getCapability(SENSOR_PROCESSING, CapabilityClient.FILTER_REACHABLE)
            )
            println("I found something!")
            println(capabilityInfo.nodes)
            loading.postValue(false)

            updateTranscriptionCapability(capabilityInfo)

        }
    }

    override fun sendTest() {
        viewModelScope.launch(Dispatchers.IO) {
            println("Start sending message")
            loading.postValue(true)
            processingNode?.also { node ->
                messageClient.sendMessage(
                    node.id,
                    SENSOR_PROCESSING,
                    "MESSAGE TEST".toByteArray()
                ).apply {
                    addOnSuccessListener {
                        println("Message sent!")
                        loading.postValue(false)
                    }
                    addOnFailureListener {
                        print("Message Failed")
                        loading.postValue(false)
                    }
                }
            }
        }
    }

    private fun updateTranscriptionCapability(capabilityInfo: CapabilityInfo) {
        processingNode = pickBestNodeId(capabilityInfo.nodes)
    }

    private fun pickBestNodeId(nodes: Set<Node>): Node? {
        return nodes.firstOrNull { it.isNearby } ?: nodes.firstOrNull()
    }

    companion object {
        const val SENSOR_PROCESSING = "sensor_processing"
    }
}

class MainViewModelFactory(private val capabilityClient: CapabilityClient, private val messageClient: MessageClient) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConnectViewModel::class.java)) {
            return ConnectViewModel(capabilityClient, messageClient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}