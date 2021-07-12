package com.acv.blockchain

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.acv.blockchain.navigation.RootRouter
import com.acv.blockchain.network.NetworkService
import com.acv.blockchain.services.DataKeys
import com.acv.blockchain.services.DatastoreService
import com.acv.blockchain.utils.ICoroutineSupport
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ICoroutineSupport {

    @Inject
    lateinit var rootRouter: RootRouter

    @Inject
    lateinit var datastoreService: DatastoreService

    @Inject
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivity()
        setContentView(R.layout.activity_main)
    }

    private fun setupActivity() {
        networkService.configureService()
        launch {
            datastoreService.readFlow(DataKeys.Token(),"").collect {
                if (it.isNotEmpty()) {
                    networkService.refreshToken()
                }
            }
        }
    }

}