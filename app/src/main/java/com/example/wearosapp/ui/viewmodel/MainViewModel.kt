package com.example.wearosapp.ui.viewmodel

import androidx.lifecycle.*
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Node
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface IMainViewModel {
    val loading: LiveData<Boolean>
    val phoneName: LiveData<String>
    val visibleID: LiveData<Boolean>
    val searchButton: LiveData<Boolean>
    fun startSearching()
    fun sendTest()
}

class MainViewModel(private val capabilityClient: CapabilityClient, private val messageClient: MessageClient) : ViewModel(), IMainViewModel {

    override val loading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    override val phoneName: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    override val visibleID: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    override val searchButton: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private var processingNode: Node? = null
        set(value) {
            field = value
            visibleID.postValue(true)
            if (value != null) {
                phoneName.postValue(value.displayName)
            }
            if (value != null) {
                searchButton.postValue(false)
            }
        }

    init {
        visibleID.postValue(false)
        searchButton.postValue(true)
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
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(capabilityClient, messageClient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}