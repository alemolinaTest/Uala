package com.amolina.domain.usecase

import app.cash.turbine.test
import com.amolina.domain.model.City
import com.amolina.domain.model.CoordDto
import com.amolina.domain.repository.CitiesRepository
import com.amolina.domain.util.Resource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class GetCitiesUseCaseTest {

    @Mock
    private lateinit var repository: CitiesRepository

    private lateinit var useCase: GetCitiesUseCase

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = GetCitiesUseCase(repository)
    }

    @Test
    fun `Successful data retrieval`() = runTest {
        val dummyCities = listOf(
            City(id = 1, name = "Test City 1", coord = CoordDto(1.0, 1.0), isFavourite = false),
            City(id = 2, name = "Test City 2", coord = CoordDto(2.0, 2.0), isFavourite = true)
        )
        val expectedFlow = flowOf(Resource.Success(dummyCities))

        `when`(repository.getCities()).thenReturn(expectedFlow)

        val result = useCase.invoke()

        result.test {
            val item = awaitItem()
            assertTrue(item is Resource.Success)
            assertEquals(dummyCities, (item as Resource.Success).data)
            awaitComplete()
        }

        verify(repository, times(1)).getCities()
    }

    @Test
    fun `Empty city list retrieval`() = runTest {
        val expectedFlow = flowOf(Resource.Success(emptyList<City>()))

        `when`(repository.getCities()).thenReturn(expectedFlow)

        val result = useCase.invoke()

        result.test {
            val item = awaitItem()
            assertTrue(item is Resource.Success)
            assertTrue((item as Resource.Success).data.isEmpty())
            awaitComplete()
        }

        verify(repository, times(1)).getCities()
    }

    @Test
    fun `Repository error emission`() = runTest {
        val expectedError = RuntimeException("API Error")
        val expectedFlow = flowOf(Resource.Error(expectedError, expectedError.message))

        `when`(repository.getCities()).thenReturn(expectedFlow)

        val result = useCase.invoke()

        result.test {
            val item = awaitItem()
            assertTrue(item is Resource.Error)
            assertEquals(expectedError, (item as Resource.Error).exception)
            awaitComplete()
        }

        verify(repository, times(1)).getCities()
    }

    @Test
    fun `Repository loading state emission`() = runTest {
        val expectedFlow = flowOf(Resource.Loading)

        `when`(repository.getCities()).thenReturn(expectedFlow)

        val result = useCase.invoke()

        result.test {
            val item = awaitItem()
            assertTrue(item is Resource.Loading)
            awaitComplete()
        }

        verify(repository, times(1)).getCities()
    }

    @Test
    fun `Repository throws exception`() = runTest {
        val exception = RuntimeException("Unexpected error")

        `when`(repository.getCities()).thenThrow(exception)

        try {
            val result = useCase.invoke()
            result.test {
                // This block will not be reached since getCities() throws immediately
            }
        } catch (e: Exception) {
            assertEquals(exception, e)
        }

        verify(repository, times(1)).getCities()
    }

    @Test
    fun `Resource Error with specific error message code`() = runTest {
        val errorMessage = "404 Not Found"
        val expectedError = RuntimeException(errorMessage)
        val expectedFlow = flowOf(Resource.Error(expectedError, errorMessage))

        `when`(repository.getCities()).thenReturn(expectedFlow)

        val result = useCase.invoke()

        result.test {
            val item = awaitItem()
            assertTrue(item is Resource.Error)
            assertEquals(errorMessage, (item as Resource.Error).message)
            awaitComplete()
        }

        verify(repository, times(1)).getCities()
    }

}