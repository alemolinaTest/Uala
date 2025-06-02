package com.amolina.data.repository

import com.amolina.data.local.CitiesDao
import com.amolina.data.local.toDomain
import com.amolina.data.local.toEntity
import com.amolina.data.remote.CitiesApiService
import com.amolina.domain.model.City
import com.amolina.domain.repository.CitiesRepository
import com.amolina.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CitiesRepositoryImpl(
    private val dao: CitiesDao,
    private val api: CitiesApiService
) : CitiesRepository {

    override fun getCities(): Flow<Resource<List<City>>> = flow {
        emit(Resource.Loading)

        // Emit local data first
        val localCities = dao.getAllCities().sortedBy { it.name }

        if (localCities.isNotEmpty()) {//still showing loading
            emit(Resource.Success(localCities.map { it.toDomain() }, isFromCache = true))
        }

        try {
            // Fetch remote cities
            val remoteCities = api.fetchCities().sortedBy { it.name }

            emit(Resource.Success(remoteCities, isFromCache = false))

            // Fetch local cities again to merge favourites
            val localCityMap = dao.getAllCities().associateBy { it.id }

            // Merge isFavourite from local into remote data
            val mergedCities = remoteCities.map { remoteCity ->
                val localCity = localCityMap[remoteCity.id]
                remoteCity.copy(isFavourite = localCity?.isFavourite ?: false)
            }

            // Insert merged data into the database
            dao.insertAll(mergedCities.map { it.toEntity() })

            // Emit updated data
            val updatedCities = dao.getAllCities()
                .sortedBy { it.name }
                .map { it.toDomain() }

            emit(Resource.Success(updatedCities, isFromCache = true))
        } catch (e: Exception) {
            emit(Resource.Error(e, e.message))
        }
    }.flowOn(Dispatchers.IO)

    override fun getFavouriteCities(): Flow<Resource<List<City>>> = flow {
        emit(Resource.Loading)
        val local = dao.getFavouriteCities().sortedBy { it.name }
        emit(Resource.Success(local.map { it.toDomain() }, isFromCache = true))
    }.flowOn(Dispatchers.IO)

    override suspend fun toggleFavourite(cityId: Int) {
        val city = dao.getCityById(cityId)
        city?.let {
            dao.updateFavourite(cityId, !it.isFavourite)
        }
    }

}