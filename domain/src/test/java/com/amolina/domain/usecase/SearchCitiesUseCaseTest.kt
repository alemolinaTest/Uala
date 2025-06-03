package com.amolina.domain.usecase

import com.amolina.domain.model.City
import com.amolina.domain.model.CoordDto
import com.amolina.domain.repository.CitiesRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import kotlin.test.assertFailsWith


@ExperimentalCoroutinesApi
class SearchCitiesUseCaseTest {

    @Mock
    private lateinit var repository: CitiesRepository

    private lateinit var useCase: SearchCitiesUseCase

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = SearchCitiesUseCase(repository)
    }

    @Test
    fun `Invoke with empty prefix and favouritesOnly true`() = runTest {
        val dummyCities = listOf(City(id = 1, name = "Test City 1", coord = CoordDto(1.0, 1.0), isFavourite = false))
        `when`(repository.searchCities("", true)).thenReturn(dummyCities)

        val result = useCase("", true)

        assertEquals(dummyCities, result)
        verify(repository, times(1)).searchCities("", true)
    }

    @Test
    fun `Invoke with empty prefix and favouritesOnly false`() = runTest {
        val dummyCities = listOf(City(id = 1, name = "Test City 1", coord = CoordDto(1.0, 1.0), isFavourite = false))
        `when`(repository.searchCities("", false)).thenReturn(dummyCities)

        val result = useCase("", false)

        assertEquals(dummyCities, result)
        verify(repository, times(1)).searchCities("", false)
    }

    @Test
    fun `Invoke with non empty prefix and favouritesOnly true`() = runTest {
        val prefix = "New"
        val dummyCities = listOf(City(id = 1, name = "Test City 1", coord = CoordDto(1.0, 1.0), isFavourite = false))
        `when`(repository.searchCities(prefix, true)).thenReturn(dummyCities)

        val result = useCase(prefix, true)

        assertEquals(dummyCities, result)
        verify(repository, times(1)).searchCities(prefix, true)
    }

    @Test
    fun `Invoke with non empty prefix and favouritesOnly false`() = runTest {
        val prefix = "San"
        val dummyCities = listOf(City(id = 1, name = "Test City 1", coord = CoordDto(1.0, 1.0), isFavourite = false))
        `when`(repository.searchCities(prefix, false)).thenReturn(dummyCities)

        val result = useCase(prefix, false)

        assertEquals(dummyCities, result)
        verify(repository, times(1)).searchCities(prefix, false)
    }

    @Test
    fun `Invoke when repository returns an empty list`() = runTest {
        `when`(repository.searchCities(anyString(), anyBoolean())).thenReturn(emptyList())

        val result = useCase("Any", false)

        assertEquals(emptyList<City>(), result)
    }

    @Test
    fun `Invoke when repository returns a list with multiple cities`() = runTest {
        val dummyCities = listOf(
            City(id = 1, name = "Test City 1", coord = CoordDto(0.0, 0.0), isFavourite = false),
            City(id = 1, name = "Test City 2", coord = CoordDto(1.0, 1.0), isFavourite = false)
        )
        `when`(repository.searchCities(anyString(), anyBoolean())).thenReturn(dummyCities)

        val result = useCase("City", false)

        assertEquals(dummyCities, result)
    }

    @Test
    fun `Invoke with prefix containing special characters`() = runTest {
        val prefix = "!@#\$%^&*()_+"
        val dummyCities = listOf(City(id = 1, name = "Test City 1", coord = CoordDto(1.0, 1.0), isFavourite = false))
        `when`(repository.searchCities(prefix, false)).thenReturn(dummyCities)

        val result = useCase(prefix, false)

        assertEquals(dummyCities, result)
        verify(repository, times(1)).searchCities(prefix, false)
    }

    @Test
    fun `Invoke with very long prefix string`() = runTest {
        val prefix = "A".repeat(1000)
        val dummyCities = listOf(City(id = 1, name = "Test City 1", coord = CoordDto(1.0, 1.0), isFavourite = false))
        `when`(repository.searchCities(prefix, false)).thenReturn(dummyCities)

        val result = useCase(prefix, false)

        assertEquals(dummyCities, result)
        verify(repository, times(1)).searchCities(prefix, false)
    }

    @Test
    fun `Invoke when repository throws an exception`() = runTest {
        val prefix = "ErrorCity"
        val exception = RuntimeException("Database error")

        `when`(repository.searchCities(prefix, false)).thenThrow(exception)

        val thrown = assertFailsWith<RuntimeException> {
            useCase(prefix, false)
        }

        assertEquals("Database error", thrown.message)
        verify(repository, times(1)).searchCities(prefix, false)
    }

    @Test
    fun `Invoke with prefix matching no cities and favouritesOnly true`() = runTest {
        val prefix = "NonMatching"
        `when`(repository.searchCities(prefix, true)).thenReturn(emptyList())

        val result = useCase(prefix, true)

        assertEquals(emptyList<City>(), result)
        verify(repository, times(1)).searchCities(prefix, true)
    }

    @Test
    fun `Invoke with prefix matching no cities and favouritesOnly false`() = runTest {
        val prefix = "NonMatching"
        `when`(repository.searchCities(prefix, false)).thenReturn(emptyList())

        val result = useCase(prefix, false)

        assertEquals(emptyList<City>(), result)
        verify(repository, times(1)).searchCities(prefix, false)
    }

    @Test
    fun `Verify repository interaction`() = runTest {
        val prefix = "Verify"
        val favouritesOnly = true
        val dummyCities = listOf(City(id = 1, name = "Test City 1", coord = CoordDto(1.0, 1.0), isFavourite = true))

        `when`(repository.searchCities(prefix, favouritesOnly)).thenReturn(dummyCities)

        val result = useCase(prefix, favouritesOnly)

        assertEquals(dummyCities, result)
        verify(repository, times(1)).searchCities(prefix, favouritesOnly)
    }
}
