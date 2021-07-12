package com.acv.blockchain.network.models

import kotlinx.serialization.Serializable

@Serializable
data class TransactionModel (
    val op: String,
    val x: TransactionInfoModel
)