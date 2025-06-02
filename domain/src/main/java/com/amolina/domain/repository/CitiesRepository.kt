package com.amolina.domain.repository

import com.amolina.domain.model.City
import com.amolina.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {
     fun getCities(): Flow<Resource<List<City>>>
     fun getFavouriteCities(): Flow<Resource<List<City>>>
     suspend fun toggleFavourite(cityId: Int)
}