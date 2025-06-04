package com.amolina.data.repository

import com.amolina.data.local.CitiesDao
import com.amolina.data.local.CityEntity
import com.amolina.data.local.toDomain
import com.amolina.data.remote.CitiesApiService
import com.amolina.domain.model.City
import com.amolina.domain.model.CoordDto
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class CitiesRepositoryImplTest {

    @Mock
    lateinit var citiesDao: CitiesDao

    @Mock
    lateinit var citiesApi: CitiesApiService

    private lateinit var repository: CitiesRepositoryImpl

    @BeforeEach
    fun setUp() {
        repository = CitiesRepositoryImpl(citiesDao, citiesApi)
    }

    @Test
    fun `toggleFavourite should toggle favourite when city exists`() = runTest {
        val cityId = 1
        val cityEntity = CityEntity(
            id = cityId, name = "TestCity", countryCode = "US",
            latitude = 0.0, longitude = 0.0, isFavourite = false
        )
        whenever(citiesDao.getCityById(cityId)).thenReturn(cityEntity)

        repository.toggleFavourite(cityId)

        verify(citiesDao).updateFavourite(cityId, true)
    }

    @Test
    fun `toggleFavourite should do nothing when city does not exist`() = runTest {
        val cityId = 99
        whenever(citiesDao.getCityById(cityId)).thenReturn(null)

        repository.toggleFavourite(cityId)

        verify(citiesDao, never()).updateFavourite(any(), any())
    }

    @Test
    fun `refreshCities should fetch from API and insert cities`() = runTest {
        val remoteCities = listOf(
            City(
                id = 1,
                name = "City1",
                countryCode = "US",
                CoordDto(latitude = 0.0, longitude = 0.0),
                isFavourite = false
            ),
            City(
                id = 2,
                name = "City2",
                countryCode = "US",
                CoordDto(latitude = 0.0, longitude = 0.0),
                isFavourite = false
            )
        )
        val localFavourites = listOf(1)

        whenever(citiesApi.fetchCities()).thenReturn(remoteCities)
        whenever(citiesDao.getFavouriteCityIds()).thenReturn(localFavourites)

        repository.refreshCities()

        verify(citiesDao).insertAll(any())
    }

    @Test
    fun `getCityById returns City when city exists`() = runTest {
        val cityId = 1
        val cityEntity = CityEntity(
            id = cityId, name = "TestCity", countryCode = "US",
            latitude = 0.0, longitude = 0.0, isFavourite = false
        )
        whenever(citiesDao.getCityById(cityId)).thenReturn(cityEntity)

        val result = repository.getCityById(cityId)

        assertEquals(cityEntity.toDomain(), result)
    }

    @Test
    fun `getCityById returns null when city does not exist`() = runTest {
        val cityId = 99
        whenever(citiesDao.getCityById(cityId)).thenReturn(null)

        val result = repository.getCityById(cityId)

        assertEquals(null, result)
    }
}
