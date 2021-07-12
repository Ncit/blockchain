package com.acv.blockchain.network.models

import kotlinx.serialization.Serializable

@Serializable
data class ProfileInfoModel(
    val info: InfoModel
)