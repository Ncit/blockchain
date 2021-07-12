package com.acv.blockchain.network

import com.acv.blockchain.network.models.AuthModel
import com.acv.blockchain.network.models.ProfileInfoModel
import com.acv.blockchain.network.models.RefreshModel
import com.acv.blockchain.network.models.SessionModel
import com.acv.blockchain.services.DataKeys
import com.acv.blockchain.services.DatastoreService
import com.acv.blockchain.utils.ICoroutineSupport
import dagger.hilt.android.scopes.ActivityScoped
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import okhttp3.*
import okio.ByteString
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

sealed class Routes(val value: String) {
    object Base: Routes("https://dev.karta.com/api")
    object Accounts: Routes("accounts/auth")
    object Refresh: Routes("accounts/sessions/refresh")
    object LogOut: Routes("accounts/sessions/end")
    object Profile: Routes("accounts/current")
}

@ActivityScoped
class NetworkService @Inject constructor() : ICoroutineSupport {

    @Inject
    lateinit var datastoreService: DatastoreService

    private var token: String = ""
    private var currentSession: SessionModel? = null

    fun configureService() {
        launch {
            datastoreService.readFlow(DataKeys.Token(),"").collect {
                token = it
            }
        }
    }

    private val client by lazy {
        HttpClient(OkHttp) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(WebSockets)
        }
    }

    fun auth(login: String, password: String, completed: () -> Unit) {

        launch {

            val response = client.post<AuthModel>("${Routes.Base.value}/${Routes.Accounts.value}") {
                body = toJson(mapOf("email" to login, "password" to password))
            }
            saveAuthData(response.token,response.expiration,response.serverTime)
            withContext(Dispatchers.Main) {
                completed()
            }
        }
    }

    private fun saveAuthData(token: String, expiration: String, serverTime: String)
    {
        datastoreService.save(token,DataKeys.Token())
        datastoreService.save(serverTime,DataKeys.ServerTime())
        datastoreService.save(expiration,DataKeys.Expiration())
    }

    fun refreshToken(completed: ((RefreshModel) -> Unit) = {}) {
        launch {
            val response = client.post<RefreshModel>("${Routes.Base.value}/${Routes.Refresh.value}") {
                header("Authorization",token)
            }
            currentSession = response.session
            saveAuthData(response.token,response.expiration,response.serverTime)
            withContext(Dispatchers.Main) {
                completed(response)
            }
        }
    }

    fun logOut(completed: ((String) -> Unit) = {}) {
        launch {
            val response = client.post<String>("${Routes.Base.value}/${Routes.LogOut.value}") {
                header("Authorization",token)
            }
            clearNetworkInfo()
            withContext(Dispatchers.Main) {
                completed(response)
            }
        }
    }

    fun profile(completed: ((ProfileInfoModel) -> Unit) = {}) {
        launch {
            val response = client.get<ProfileInfoModel>("${Routes.Base.value}/${Routes.Profile.value}") {
                header("Authorization",token)
            }
            currentSession = response.info.session
            withContext(Dispatchers.Main) {
                completed(response)
            }
        }
    }

//    fun transactions() {
//        launch {
//
//            val client = OkHttpClient.Builder()
//                .readTimeout(3, TimeUnit.SECONDS)
//                //.sslSocketFactory() - ? нужно ли его указывать дополнительно
//                .build()
//            val request = Request.Builder()
//                .url("wss://ws.blockchain.info/inv") // 'wss' - для защищенного канала
//                .build()
//            val wsListener = SocketListener()
//            val webSocket = client.newWebSocket(request, wsListener)
//
////            val client = HttpClient {
////                install(WebSockets)
////            }
////            client.webSocket(method = HttpMethod.Get, host = "ws.blockchain.info", path = "/inv") {
////                send("{op: ping}")
////
////            }
//
////            client.webSocket(
////                host = "ws.blockchain.info",path = "inv"
////            ) { // this: DefaultClientWebSocketSession
////
//////                // Send text frame.
////                send("{op: ping}")
//////
//////                // Send text frame.
//////                send(Frame.Text("Hello World"))
////
////                // Send binary frame.
//////            send(Frame.Binary(...))
////
////                // Receive frame.
////                val frame = incoming.receive()
////                print(frame)
//////                when (frame) {
//////                    is Frame.Text -> println(frame.readText())
//////                    is Frame.Binary -> println(frame.readBytes())
//////                }
////            }
//        }
//    }

    private fun clearNetworkInfo() {
        token = ""
        currentSession = null
        datastoreService.clear()
    }

    private fun toJson(data: Map<String, Any?>): String {
        return JSONObject(data).toString()
    }
}