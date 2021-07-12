package com.acv.blockchain.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshModel(
    val token: String,
    val expiration: String,
    @SerialName("server_time")
    val serverTime: String,
    val session: SessionModel
)