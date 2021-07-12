package com.acv.blockchain.network

import com.acv.blockchain.network.models.TransactionModel
import com.acv.blockchain.utils.ICoroutineSupport
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.StateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ActivityScoped
class SocketService @Inject constructor(): ICoroutineSupport {

    private val client = OkHttpClient.Builder()
        .readTimeout(3, TimeUnit.SECONDS)
        .build()

    private val request = Request.Builder()
        .url("wss://ws.blockchain.info/inv")
        .build()

    private val wsListener = SocketListener()

    private var webSocket: WebSocket? = null

    fun subscribe(socketFlow: (StateFlow<TransactionModel?>) -> Unit) {
        socketFlow(wsListener.transactionsFlow)
        webSocket = client.newWebSocket(request, wsListener)
    }

    fun stopSubscribe() {
        webSocket?.cancel()
        webSocket = null
    }
}