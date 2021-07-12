package com.acv.blockchain.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrowserModel(
    val bot: Boolean,
    val browser: String,
    @SerialName("browser_version")
    val browserVersion: String,
    val engine: String,
    @SerialName("engine_version")
    val engineVersion: String,
    val mobile: Boolean,
    val os: OSModel,
    val platform: String
)