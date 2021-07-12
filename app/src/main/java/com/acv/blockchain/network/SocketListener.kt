package com.acv.blockchain.network

import com.acv.blockchain.network.models.TransactionModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class SocketListener @Inject constructor(): WebSocketListener() {

    private val _transactionsFlow = MutableStateFlow<TransactionModel?>(null)
    val transactionsFlow: StateFlow<TransactionModel?> = _transactionsFlow

    private val jsonDecoder = Json{ ignoreUnknownKeys = true }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        webSocket.send("{op: unconfirmed_sub}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        _transactionsFlow.value = jsonDecoder.decodeFromString(TransactionModel.serializer(),text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        print(response)
    }
}