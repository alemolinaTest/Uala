package com.amolina.domain.repository

import androidx.paging.PagingData
import com.amolina.domain.model.City
import com.amolina.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {
     fun getCities(): Flow<Resource<List<City>>>
     fun getFavouriteCities(): Flow<Resource<List<City>>>
     suspend fun toggleFavourite(cityId: Int)
     fun getCitiesPaged(): Flow<PagingData<City>>  // Add this!

}