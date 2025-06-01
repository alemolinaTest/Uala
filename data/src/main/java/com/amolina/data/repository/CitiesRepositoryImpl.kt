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

class CitiesRepositoryImpl (
    private val dao: CitiesDao,
    private val api: CitiesApiService
) : CitiesRepository {

    override fun getCities(): Flow<Resource<List<City>>> = flow {
        emit(Resource.Loading) // emit loading state
        val local = dao.getAllCities()
        //emit local data first
        emit(Resource.Success(local.map { it.toDomain() }, isFromCache = true))

        try {
            val remote = api.fetchCities()
            dao.insertAll(remote.map { it.toEntity() })
            val updated = dao.getAllCities().map { it.toDomain() }
            //emit updated local data
            emit(Resource.Success(updated, isFromCache = false))
        } catch (e: Exception) {
            //emit error state
            emit(Resource.Error(e, e.message))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun toggleFavourite(cityId: Int) {
        val city = dao.getCityById(cityId)
        city?.let {
            dao.updateFavourite(cityId, !it.isFavourite)
        }
    }

}