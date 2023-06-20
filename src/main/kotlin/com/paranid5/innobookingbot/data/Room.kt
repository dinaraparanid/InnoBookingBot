package com.paranid5.innobookingbot.data

import kotlinx.serialization.Serializable

@Serializable
data class Room(val name: String, val id: String, val type: String, val capacity: Int)