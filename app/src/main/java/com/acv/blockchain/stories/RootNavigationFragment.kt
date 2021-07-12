package com.acv.blockchain.stories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.acv.blockchain.R
import com.acv.blockchain.navigation.RootRouter
import com.acv.blockchain.services.DataKeys
import com.acv.blockchain.services.DatastoreService
import com.acv.blockchain.utils.ICoroutineSupport
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class RootNavigationFragment : Fragment(R.layout.fragment_root_navigation), ICoroutineSupport {

    @Inject
    lateinit var rootRouter: RootRouter

    @Inject
    lateinit var datastoreService: DatastoreService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkAuthState()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun checkAuthState() {
        launch { scope ->
            datastoreService.readFlow(DataKeys.Token(),"").collect {
                when {
                    it.isNotEmpty() -> {
                        rootRouter.navigateMainFromRoot()
                    }
                    else -> {
                        rootRouter.navigateLogin()
                    }
                }
                scope.cancel()
            }
        }
    }

}