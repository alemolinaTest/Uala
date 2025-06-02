package com.amolina.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.amolina.data.local.CitiesDao
import com.amolina.data.local.CityEntity
import com.amolina.data.local.toEntity
import com.amolina.data.remote.CitiesApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class CitiesRemoteMediator(
    private val dao: CitiesDao,
    private val apiService: CitiesApiService,
    val ioDispatcher: CoroutineDispatcher
) : RemoteMediator<Int, CityEntity>() {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CityEntity>
    ): MediatorResult {
        if (loadType != LoadType.REFRESH) {
            return MediatorResult.Success(endOfPaginationReached = true)
        }
        return try {
            withContext(ioDispatcher) {
                val cities = apiService.fetchCities()
                dao.insertAll(cities.map { it.toEntity() })
            }
            MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

}
