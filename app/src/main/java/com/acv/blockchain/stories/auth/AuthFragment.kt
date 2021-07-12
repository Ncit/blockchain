package com.acv.blockchain.stories.auth

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.acv.blockchain.R
import com.acv.blockchain.databinding.AuthFragmentBinding
import com.acv.blockchain.navigation.RootRouter
import com.acv.blockchain.network.NetworkService
import com.acv.blockchain.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.auth_fragment) {

    private val viewModel: AuthViewModel by viewModels()
    private val binding by viewBinding(AuthFragmentBinding::bind)

    @Inject
    lateinit var rootRouter: RootRouter

    @Inject
    lateinit var networkService: NetworkService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragment()
    }

    private fun setupFragment() {
        clearAllErrors()
        binding.apply {
            authButton.setOnClickListener {
                validateLoginData(::auth)
            }
            passwordEdit.addTextChangedListener {
                passwordEdit.error = null
            }
            loginEdit.addTextChangedListener {
                loginEdit.error = null
            }
        }
    }

    private fun clearAllErrors() {
        binding.loginEdit.error = null
        binding.passwordEdit.error = null
    }

    private fun validateLoginData(success: () -> Unit) {
        binding.apply {
            when {
                loginEdit.text.isNullOrEmpty() -> {
                    loginEdit.error = getString(R.string.error_hint)
                }
                passwordEdit.text.isNullOrEmpty() -> {
                    passwordEdit.error = getString(R.string.error_hint)
                }
                else -> {
                    success()
                }
            }
        }
    }

    private fun auth() {
        val login = binding.loginEdit.text?.toString()
        val password = binding.passwordEdit.text?.toString()
        if (login != null && password != null) {
            networkService.auth(login, password) {
                rootRouter.navigateMainFromLogin()
            }
        }
    }

}