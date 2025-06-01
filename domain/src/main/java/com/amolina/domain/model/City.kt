package com.amolina.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Int,
    val name: String,
    val country: String,
    val isFavourite: Boolean
)