package com.amolina.domain.usecase

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.amolina.domain.model.City
import com.amolina.domain.model.CoordDto
import com.amolina.domain.repository.CitiesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
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
        val dummyCities = listOf(
            City(id = 1, name = "Test City 1", coord = CoordDto(1.0, 1.0), isFavourite = false)
        )
        `when`(repository.searchCitiesPaged("", true)).thenReturn(flowOf(PagingData.from(dummyCities)))

        val resultFlow = useCase("", true)
        val result = resultFlow.asSnapshot()

        assertEquals(dummyCities, result)
        verify(repository, times(1)).searchCitiesPaged("", true)
    }

    @Test
    fun `Invoke with empty prefix and favouritesOnly false`() = runTest {
        val dummyCities = listOf(
            City(id = 1, name = "Test City 1", coord = CoordDto(1.0, 1.0), isFavourite = false)
        )
        `when`(repository.searchCitiesPaged("", false)).thenReturn(flowOf(PagingData.from(dummyCities)))

        val resultFlow = useCase("", false)
        val result = resultFlow.asSnapshot()

        assertEquals(dummyCities, result)
        verify(repository, times(1)).searchCitiesPaged("", false)
    }

    @Test
    fun `Invoke with non empty prefix and favouritesOnly true`() = runTest {
        val prefix = "New"
        val dummyCities = listOf(
            City(id = 1, name = "Test City 1", coord = CoordDto(1.0, 1.0), isFavourite = false)
        )
        `when`(repository.searchCitiesPaged(prefix, true)).thenReturn(flowOf(PagingData.from(dummyCities)))

        val resultFlow = useCase(prefix, true)
        val result = resultFlow.asSnapshot()

        assertEquals(dummyCities, result)
        verify(repository, times(1)).searchCitiesPaged(prefix, true)
    }

    @Test
    fun `Invoke with non empty prefix and favouritesOnly false`() = runTest {
        val prefix = "San"
        val dummyCities = listOf(
            City(id = 1, name = "Test City 1", coord = CoordDto(1.0, 1.0), isFavourite = false)
        )
        `when`(repository.searchCitiesPaged(prefix, false)).thenReturn(flowOf(PagingData.from(dummyCities)))

        val resultFlow = useCase(prefix, false)
        val result = resultFlow.asSnapshot()

        assertEquals(dummyCities, result)
        verify(repository, times(1)).searchCitiesPaged(prefix, false)
    }

    @Test
    fun `Invoke when repository returns an empty list`() = runTest {
        `when`(repository.searchCitiesPaged(anyString(), anyBoolean()))
            .thenReturn(flowOf(PagingData.from(emptyList())))

        val resultFlow = useCase("Any", false)
        val result = resultFlow.asSnapshot()

        assertEquals(emptyList<City>(), result)
    }

    @Test
    fun `Invoke when repository returns a list with multiple cities`() = runTest {
        val dummyCities = listOf(
            City(id = 1, name = "Test City 1", coord = CoordDto(0.0, 0.0), isFavourite = false),
            City(id = 2, name = "Test City 2", coord = CoordDto(1.0, 1.0), isFavourite = false)
        )
        `when`(repository.searchCitiesPaged(anyString(), anyBoolean()))
            .thenReturn(flowOf(PagingData.from(dummyCities)))

        val resultFlow = useCase("City", false)
        val result = resultFlow.asSnapshot()

        assertEquals(dummyCities, result)
    }

    @Test
    fun `Invoke with prefix containing special characters`() = runTest {
        val prefix = "!@#\$%^&*()_+"
        val dummyCities = listOf(
            City(id = 1, name = "Test City 1", coord = CoordDto(1.0, 1.0), isFavourite = false)
        )
        `when`(repository.searchCitiesPaged(prefix, false))
            .thenReturn(flowOf(PagingData.from(dummyCities)))

        val resultFlow = useCase(prefix, false)
        val result = resultFlow.asSnapshot()

        assertEquals(dummyCities, result)
        verify(repository, times(1)).searchCitiesPaged(prefix, false)
    }

    @Test
    fun `Invoke with very long prefix string`() = runTest {
        val prefix = "A".repeat(1000)
        val dummyCities = listOf(
            City(id = 1, name = "Test City 1", coord = CoordDto(1.0, 1.0), isFavourite = false)
        )
        `when`(repository.searchCitiesPaged(prefix, false))
            .thenReturn(flowOf(PagingData.from(dummyCities)))

        val resultFlow = useCase(prefix, false)
        val result = resultFlow.asSnapshot()

        assertEquals(dummyCities, result)
        verify(repository, times(1)).searchCitiesPaged(prefix, false)
    }

    @Test
    fun `Invoke when repository throws an exception`() = runTest {
        val prefix = "ErrorCity"
        val exception = RuntimeException("Database error")
        `when`(repository.searchCitiesPaged(prefix, false))
            .thenThrow(exception)

        assertFailsWith<RuntimeException> {
            useCase(prefix, false).asSnapshot()
        }
        verify(repository, times(1)).searchCitiesPaged(prefix, false)
    }

    @Test
    fun `Invoke with prefix matching no cities and favouritesOnly true`() = runTest {
        val prefix = "NonMatching"
        `when`(repository.searchCitiesPaged(prefix, true))
            .thenReturn(flowOf(PagingData.from(emptyList())))

        val resultFlow = useCase(prefix, true)
        val result = resultFlow.asSnapshot()

        assertEquals(emptyList<City>(), result)
        verify(repository, times(1)).searchCitiesPaged(prefix, true)
    }

    @Test
    fun `Invoke with prefix matching no cities and favouritesOnly false`() = runTest {
        val prefix = "NonMatching"
        `when`(repository.searchCitiesPaged(prefix, false))
            .thenReturn(flowOf(PagingData.from(emptyList())))

        val resultFlow = useCase(prefix, false)
        val result = resultFlow.asSnapshot()

        assertEquals(emptyList<City>(), result)
        verify(repository, times(1)).searchCitiesPaged(prefix, false)
    }

    @Test
    fun `Verify repository interaction`() = runTest {
        val prefix = "Verify"
        val favouritesOnly = true
        val dummyCities = listOf(
            City(id = 1, name = "Test City 1", coord = CoordDto(1.0, 1.0), isFavourite = true)
        )
        `when`(repository.searchCitiesPaged(prefix, favouritesOnly))
            .thenReturn(flowOf(PagingData.from(dummyCities)))

        val resultFlow = useCase(prefix, favouritesOnly)
        val result = resultFlow.asSnapshot()

        assertEquals(dummyCities, result)
        verify(repository, times(1)).searchCitiesPaged(prefix, favouritesOnly)
    }
}
