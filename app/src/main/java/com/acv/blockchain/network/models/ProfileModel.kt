package com.acv.blockchain.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileModel (
    @SerialName("profile_id")
    val profileId: String,
    @SerialName("account_id")
    val accountId: String,
    @SerialName("profile_type")
    val profileType: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    val location: String,
    val gender: String,
//    @SerialName("phone_country")
//    val phoneCountry: String?,
//    @SerialName("phone_number")
//    val phoneNumber: String?,
//    val email: String,
//    @SerialName("avatar_url")
//    val avatarUrl: String?,
//    @SerialName("langs_spoken_names")
//    val langsSpokenNames: List<String>,
//    @SerialName("joined_at")
//    val joinedAt: String,
//    @SerialName("online_status")
//    val onlineStatus: String,
//    @SerialName("guest_score")
//    val guest_score: String?,
//    @SerialName("reviews_count")
//    val reviewsCount: Int,
//    @SerialName("is_partner")
//    val isPartner: Boolean,
//    @SerialName("billing_info")
//    val billingInfo: String,
//    @SerialName("country")
//    val country: String?,
//    @SerialName("nationality")
//    val nationality: String?,
)