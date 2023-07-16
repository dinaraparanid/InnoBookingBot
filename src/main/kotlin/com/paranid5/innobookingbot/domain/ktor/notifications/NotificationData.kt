package com.paranid5.innobookingbot.domain.ktor.notifications

import com.paranid5.innobookingbot.data.BookResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationData(
    @SerialName("tg_id") val tgId: String,
    @SerialName("booking") val booking: BookResponse
)
