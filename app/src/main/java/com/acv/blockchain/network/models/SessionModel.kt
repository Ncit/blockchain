package com.acv.blockchain.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionModel(
    @SerialName("session_id")
    val sessionId: String,
    @SerialName("account_id")
    val accountId: String,
    @SerialName("client_ip")
    val clientIp: String,
    @SerialName("client_country")
    val clientCountry: String,
    @SerialName("client_country_iso")
    val clientCountryIso: String,
    @SerialName("client_city")
    val clientCity: String,
    @SerialName("client_agent")
    val clientAgent: String,
    val verifications: List<String>,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("expiry_at")
    val expiryAt: String,
    @SerialName("last_use")
    val lastUse: String,
    val device: String?,
    val browser: BrowserModel
)