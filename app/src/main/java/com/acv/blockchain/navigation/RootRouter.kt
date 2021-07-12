package com.acv.blockchain.navigation

import android.content.Context
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.acv.blockchain.MainActivity
import com.acv.blockchain.R
import com.acv.blockchain.stories.RootNavigationFragmentDirections
import com.acv.blockchain.stories.auth.AuthFragmentDirections
import com.acv.blockchain.utils.ICoroutineSupport
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class RootRouter @Inject constructor(@ActivityContext val context: Context): ICoroutineSupport {

    private val navController by lazy {
        (context as MainActivity).findNavController(R.id.nav_host_fragment)
    }

    fun navigateLogin() {
        navController.popBackStack(R.id.rootNavigationFragment, false)
        navigate(RootNavigationFragmentDirections.actionGlobalAuthFragment())
    }

    fun navigateMainFromLogin() {
        navigate(AuthFragmentDirections.actionAuthFragmentToMainFragment())
    }

    fun navigateMainFromRoot() {
        navigate(RootNavigationFragmentDirections.actionRootNavigationFragmentToMainFragment())
    }

    private fun navigate(directions: NavDirections) {
        launchOnMain {
            navController.navigate(directions)
        }
    }

}