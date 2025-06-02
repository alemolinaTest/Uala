package com.amolina.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class City(
    @SerialName("_id") val id: Int = 0,
    val name: String,
    val countryCode: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isFavourite: Boolean = false
)