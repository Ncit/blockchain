package com.acv.blockchain.network.models

import kotlinx.serialization.Serializable

@Serializable
data class TransactionInfoModel(
    val time: Long,
    val hash: String,
    val out: List<TransactionOutInfoModel>
)