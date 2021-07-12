package com.acv.blockchain.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OSModel(
    @SerialName("full_name")
    val fullName: String,
    val name: String,
    val version: String
)