package com.acv.blockchain.stories.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acv.blockchain.R
import com.acv.blockchain.databinding.MainFragmentBinding
import com.acv.blockchain.navigation.RootRouter
import com.acv.blockchain.network.NetworkService
import com.acv.blockchain.network.SocketService
import com.acv.blockchain.network.models.ProfileInfoModel
import com.acv.blockchain.network.models.TransactionModel
import com.acv.blockchain.utils.ICoroutineSupport
import com.acv.blockchain.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment), ICoroutineSupport {

    private val viewModel: MainViewModel by viewModels()
    private val binding by viewBinding(MainFragmentBinding::bind)

    @Inject
    lateinit var networkService: NetworkService

    @Inject
    lateinit var socketService: SocketService

    @Inject
    lateinit var rootRouter: RootRouter

    @Inject
    lateinit var transactionsAdapter: TransactionsAdapter

    private var transactionJob: Job? = null
    private var sumJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragment()
    }

    override fun onResume() {
        super.onResume()
        startAction()
    }

    private fun consumeTransaction(transaction: TransactionModel) {
        var total: Long = 0
        transaction.x.out.forEach {
            total += it.value
        }
        viewModel.appendSum(total)
        transactionsAdapter.add(transaction)
        binding.dataList.smoothScrollToPosition(transactionsAdapter.itemCount)
    }

    override fun onPause() {
        super.onPause()
        stopAction()
    }

    private fun setupFragment() {
        observeProfile()
        configureButtons()
        networkService.profile {
            viewModel.setup(it)
        }
    }

    private fun configureButtons() {
        binding.apply {
            start.setOnClickListener {
                startAction()
            }
            stop.setOnClickListener {
                stopAction()
            }
            reset.setOnClickListener {
                resetAction()
            }
        }
    }

    private fun resetAction() {
        transactionsAdapter.clear()
        viewModel.resetSum()
        binding.sumValue.text = "0"
    }

    private fun stopAction() {
        socketService.stopSubscribe()
        transactionJob?.cancel()
        transactionJob = null
        sumJob?.cancel()
        sumJob = null
    }

    private fun startAction() {
        socketService.subscribe { transactionFlow ->
            transactionJob = launch {
                transactionFlow.collect {
                    withContext(Dispatchers.Main) {
                        it?.let { transaction ->
                            consumeTransaction(transaction)
                        }
                    }
                }
            }
        }

        sumJob = launchOnMain {
            viewModel.totalSum.collect {
                binding.sumValue.text = "$it"
            }
        }
    }

    private fun observeProfile() {
        launchOnMain {
            viewModel.profile.collect {
                withContext(Dispatchers.Main) {
                    it?.let {
                        setupUI(it)
                    }
                }
            }
        }
    }

    private fun setupUI(profileInfoModel: ProfileInfoModel) {
        binding.apply {
            val profile = profileInfoModel.info.profiles.firstOrNull()
            profile?.let {
                nameValue.text = it.firstName
                lastNameValue.text = it.lastName
            }

            logOut.setOnClickListener {
                logOutAction()
            }

            dataList.layoutManager = LinearLayoutManager(requireActivity())
            dataList.setHasFixedSize(true)
            dataList.adapter = transactionsAdapter
            dataList.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    private fun logOutAction() {
        networkService.logOut {
            rootRouter.navigateLogin()
        }
    }
}