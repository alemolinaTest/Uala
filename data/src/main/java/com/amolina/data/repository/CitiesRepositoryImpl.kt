package com.amolina.data.repository


import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.amolina.data.local.CitiesDao
import com.amolina.data.local.toDomain
import com.amolina.data.local.toEntity
import com.amolina.data.remote.CitiesApiService
import com.amolina.domain.model.City
import com.amolina.domain.repository.CitiesRepository
import com.amolina.domain.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class CitiesRepositoryImpl(
    private val dao: CitiesDao,
    private val api: CitiesApiService,
    private val ioDispatcher: CoroutineDispatcher
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
    }.flowOn(ioDispatcher)

    override fun getFavouriteCities(): Flow<Resource<List<City>>> = flow {
        emit(Resource.Loading)
        val local = dao.getFavouriteCities().sortedBy { it.name }
        emit(Resource.Success(local.map { it.toDomain() }, isFromCache = true))
    }.flowOn(ioDispatcher)

    override suspend fun toggleFavourite(cityId: Int) {
        val city = dao.getCityById(cityId)
        city?.let {
            dao.updateFavourite(cityId, !it.isFavourite)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getCitiesPaged(): Flow<PagingData<City>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false// disables null placeholders, speeds up UI binding
            ),
            remoteMediator = CitiesRemoteMediator(dao, api, ioDispatcher),
            pagingSourceFactory = { dao.getAllCitiesPaged() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun searchCities(prefix: String, favouritesOnly: Boolean): List<City> {
        val localCities = if (favouritesOnly) {
            dao.searchFavouriteCities(prefix)
        } else {
            dao.searchCities(prefix)
        }
        return localCities.map { it.toDomain() }
    }

}