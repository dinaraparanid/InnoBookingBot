package com.paranid5.innobookingbot.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookQueryRequest(val filter: Filter) {
    @Serializable
    data class Filter(
        @SerialName("started_at_or_after") val startedAtOrAfter: Instant,
        @SerialName("ended_at_or_before") val endedAtOrBefore: Instant,
        @SerialName("owner_email_in") val ownerEmailIn: List<String> = listOf(),
        @SerialName("room_id_in") val roomIdIn: List<String> = listOf(),
    )

    constructor(
        startedAtOrAfter: Instant,
        endedAtOrBefore: Instant,
        roomIdIn: List<String> = listOf(),
        ownerEmailIn: List<String> = listOf()
    ) : this(
        Filter(
            startedAtOrAfter = startedAtOrAfter,
            endedAtOrBefore = endedAtOrBefore,
            roomIdIn = roomIdIn,
            ownerEmailIn = ownerEmailIn)
    )
}
