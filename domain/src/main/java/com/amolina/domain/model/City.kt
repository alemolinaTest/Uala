package com.amolina.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class City(
    @SerialName("_id") val id: Int = 0,
    val name: String,
    @SerialName("country") val countryCode: String = "",
    val coord: CoordDto,
    val isFavourite: Boolean = false
)

@Serializable
data class CoordDto(
    @SerialName("lat") val latitude: Double = 0.0,
    @SerialName("lon") val longitude: Double = 0.0,
)