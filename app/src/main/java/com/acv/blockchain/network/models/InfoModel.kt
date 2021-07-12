package com.acv.blockchain.network.models

import kotlinx.serialization.Serializable

@Serializable
data class InfoModel (
    val session: SessionModel,
    val account: AccountModel,
    val profiles: List<ProfileModel>
)