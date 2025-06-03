package com.amolina.data.repository

import com.amolina.data.local.CitiesDao
import com.amolina.data.local.CityEntity
import com.amolina.data.local.toDomain
import com.amolina.data.remote.CitiesApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

@ExperimentalCoroutinesApi
class CitiesRepositoryImplTest {

    @Mock
    private lateinit var dao: CitiesDao

    @Mock
    private lateinit var apiService: CitiesApiService

    private lateinit var repository: CitiesRepositoryImpl

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = CitiesRepositoryImpl(dao, apiService, testDispatcher)
    }

    @Test
    fun `searchCities with favouritesOnly false calls dao searchCities`() = runTest {
        val prefix = "Lon"
        val dummyEntities = listOf(
            CityEntity(
                id = 1,
                name = "London",
                countryCode = "US",
                latitude = 0.0,
                longitude = 0.0,
                isFavourite = false
            )
        )
        val expectedDomain = dummyEntities.map { it.toDomain() }

        `when`(dao.searchCities(prefix)).thenReturn(dummyEntities)

        val result = repository.searchCities(prefix, favouritesOnly = false)

        verify(dao, times(1)).searchCities(prefix)
        verify(dao, never()).searchFavouriteCities(anyString())
        assertEquals(expectedDomain, result)
    }

    @Test
    fun `searchCities with favouritesOnly true calls dao searchFavouriteCities`() = runTest {
        val prefix = "Par"
        val dummyEntities = listOf(
            CityEntity(
                id = 2,
                name = "Paris",
                countryCode = "FR",
                latitude = 0.0,
                longitude = 0.0,
                isFavourite = true
            )
        )
        val expectedDomain = dummyEntities.map { it.toDomain() }

        `when`(dao.searchFavouriteCities(prefix)).thenReturn(dummyEntities)

        val result = repository.searchCities(prefix, favouritesOnly = true)

        verify(dao, times(1)).searchFavouriteCities(prefix)
        verify(dao, never()).searchCities(anyString())
        assertEquals(expectedDomain, result)
    }

    @Test
    fun `searchCities returns empty list for no matches`() = runTest {
        val prefix = "NonExistentCity"
        `when`(dao.searchCities(prefix)).thenReturn(emptyList())

        val result = repository.searchCities(prefix, favouritesOnly = false)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `searchCities returns correctly mapped domain objects`() = runTest {
        val prefix = "Test"
        val dummyEntities = listOf(
            CityEntity(
                id = 1,
                name = "California",
                countryCode = "US",
                latitude = 0.0,
                longitude = 0.0,
                isFavourite = true
            )
        )

        val expectedDomain = dummyEntities.map { it.toDomain() }

        `when`(dao.searchCities(prefix)).thenReturn(dummyEntities)

        val result = repository.searchCities(prefix, favouritesOnly = false)

        assertEquals(expectedDomain, result)
    }

    @Test
    fun `searchCities handles special characters in prefix`() = runTest {
        val prefix = "%@$#"
        `when`(dao.searchCities(prefix)).thenReturn(emptyList())

        val result = repository.searchCities(prefix, favouritesOnly = false)

        assertTrue(result.isEmpty())
        verify(dao, times(1)).searchCities(prefix)
    }
}