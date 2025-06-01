package com.amolina.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amolina.domain.model.City

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val country: String,
    val isFavourite: Boolean = false
)

fun CityEntity.toDomain(): City = City(id, name, country, isFavourite)
fun City.toEntity() = CityEntity(id, name, country, isFavourite)
