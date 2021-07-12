package com.acv.blockchain.utils

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

interface ICoroutineSupport {
    private val coroutineScope: CoroutineScope
        get() = buildScope()

    private val coroutineContext
        get() = buildContext()

    fun launch(block: suspend (CoroutineScope) -> Unit): Job {
        return coroutineScope.launch(coroutineContext) {
            block(this)
        }
    }

    fun launchOnMain(block: suspend () -> Unit): Job {
        return coroutineScope.launch(Dispatchers.Main) {
            block()
        }
    }

    private fun buildContext(): CoroutineContext {
        return Dispatchers.IO + buildJob()
    }

    private fun buildJob(): Job {
        return SupervisorJob()
    }

    private fun buildScope(): CoroutineScope {
        return CoroutineScope(coroutineContext)
    }
}