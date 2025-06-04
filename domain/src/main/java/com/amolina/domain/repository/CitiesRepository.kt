package com.amolina.domain.repository

import androidx.paging.PagingData
import com.amolina.domain.model.City
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {
     fun getCitiesPaged(): Flow<PagingData<City>>
     fun getFavouriteCitiesPaged(): Flow<PagingData<City>>
     suspend fun toggleFavourite(cityId: Int)
     fun searchCitiesPaged(prefix: String, favouritesOnly: Boolean): Flow<PagingData<City>>
     suspend fun getCityById(cityId: Int): City?
     suspend fun refreshCities()
}