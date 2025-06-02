package com.amolina.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.amolina.domain.model.City
import com.amolina.domain.model.CoordDto

@Entity(
    tableName = "cities",
    indices = [Index(value = ["name"])]
)
data class CityEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val countryCode: String,
    val latitude: Double,
    val longitude: Double,
    val isFavourite: Boolean = false
)

fun CityEntity.toDomain(): City =
    City(id, name, countryCode, coord = CoordDto(latitude, longitude), isFavourite)

fun City.toEntity(): CityEntity =
    CityEntity(id, name, countryCode, coord.latitude, coord.longitude, isFavourite)
