package com.amolina.data.repository

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CitiesRepositoryImpl(
    private val dao: CitiesDao,
    private val api: CitiesApiService,
) : CitiesRepository {

    override fun getCitiesPaged(): Flow<PagingData<City>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { dao.getAllCitiesPaged() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun getFavouriteCitiesPaged(): Flow<PagingData<City>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { dao.getFavouriteCitiesPaged() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun searchCitiesPaged(
        prefix: String,
        favouritesOnly: Boolean
    ): Flow<PagingData<City>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                if (favouritesOnly) {
                    dao.searchFavouriteCitiesPaged(prefix)
                } else {
                    dao.searchCitiesPaged(prefix)
                }
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun toggleFavourite(cityId: Int) {
        val city = dao.getCityById(cityId)
        city?.let {
            dao.updateFavourite(cityId, !it.isFavourite)
        }
    }

    override suspend fun refreshCities() {
        val remoteCities = api.fetchCities()

        val localCityMap = dao.getFavouriteCityIds().associateWith { true }

        val mergedCities = remoteCities.map { remoteCity ->
            remoteCity.copy(isFavourite = localCityMap.containsKey(remoteCity.id))
        }

        dao.insertAll(mergedCities.map { it.toEntity() })
    }

    override suspend fun getCityById(cityId: Int): City? = dao.getCityById(cityId)?.toDomain()
}