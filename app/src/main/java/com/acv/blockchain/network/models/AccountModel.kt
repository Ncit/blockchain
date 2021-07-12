package com.acv.blockchain.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountModel(
    @SerialName("account_id")
    val accountId: String,
    @SerialName("account_type")
    val accountType: String,
    val email: String,
    @SerialName("email_verified")
    val emailVerified: Boolean,
    val phone: String,
    @SerialName("totp_verified")
    val totpVerified: Boolean,
    @SerialName("2fa_method")
    val faMethod: String,
    val password: String?,
    @SerialName("created_at")
    val createdAt: String,
)