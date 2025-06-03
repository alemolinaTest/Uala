package com.amolina.domain.usecase

import androidx.paging.PagingData
import app.cash.turbine.test
import com.amolina.domain.model.City
import com.amolina.domain.model.CoordDto
import com.amolina.domain.repository.CitiesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class GetCitiesPagedUseCaseTest {

    @Mock
    private lateinit var mockCitiesRepository: CitiesRepository

    private lateinit var getCitiesPagedUseCase: GetCitiesPagedUseCase
    private lateinit var closeable: AutoCloseable

    // TestScope for more control in cancellation tests
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)


    @BeforeEach
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        getCitiesPagedUseCase = GetCitiesPagedUseCase(mockCitiesRepository)
    }

    @AfterEach
    fun tearDown() {
        closeable.close()
        // Ensure that the mock is reset after each test if needed,
        // or ensure verifications are specific to prevent test interference.
        // For simple pass-through, this might not be strictly necessary if mocks are always re-stubbed.
    }

    @Test
    @DisplayName("Successful data retrieval: returns Flow of PagingData from repository")
    fun `Successful data retrieval`() = testScope.runTest {
        // Arrange
        val cityList = listOf(
            City(1, "London", "UK", CoordDto(1.0, 1.0)),
            City(2, "Paris", "France", CoordDto(2.0, 2.0))
        )
        val fakePagingData = PagingData.from(cityList)
        val expectedFlow = flowOf(fakePagingData)
        `when`(mockCitiesRepository.getCitiesPaged()).thenReturn(expectedFlow)

        // Act
        val resultFlow = getCitiesPagedUseCase()

        // Assert
        resultFlow.test {
            Assert.assertEquals(fakePagingData, awaitItem())
            awaitComplete()
        }
        verify(mockCitiesRepository).getCitiesPaged()
    }

    @Test
    @DisplayName("Empty data set: handles empty PagingData flow from repository")
    fun `Empty data set`() = testScope.runTest {
        // Arrange
        val emptyPagingData = PagingData.empty<City>()
        val expectedFlow = flowOf(emptyPagingData)
        `when`(mockCitiesRepository.getCitiesPaged()).thenReturn(expectedFlow)

        // Act
        val resultFlow = getCitiesPagedUseCase()

        // Assert
        resultFlow.test {
            val emittedItem = awaitItem()
            Assert.assertNotNull(emittedItem) // Check it's not null
            // Further checks for emptiness would involve a PagingDataDiffer or collecting items
            // For a unit test of a simple pass-through, this is often sufficient.
            awaitComplete()
        }
        verify(mockCitiesRepository).getCitiesPaged()
    }

    @Test
    @DisplayName("Error handling: propagates exception from repository")
    fun `Error handling`() = testScope.runTest {
        // Arrange
        val expectedException = RuntimeException("Network Error")
        `when`(mockCitiesRepository.getCitiesPaged()).thenReturn(flow { throw expectedException })

        // Act
        val resultFlow = getCitiesPagedUseCase()

        // Assert
        resultFlow.test {
            Assert.assertEquals(expectedException, awaitError())
        }
        verify(mockCitiesRepository).getCitiesPaged()
    }

    @Test
    @DisplayName("Pagination functionality: (Unit Test Scope) Verifies PagingData is passed through")
    fun `Pagination functionality`() = testScope.runTest {
        // Note: Fully testing Paging 3's loading mechanism (scrolling, placeholders, etc.)
        // typically requires instrumented tests with a PagingDataDiffer.
        // For a unit test of this UseCase, we verify that the PagingData object,
        // which encapsulates this functionality, is correctly passed through.
        // Arrange
        val cityList = listOf(City(1,"Rome", "Italy", CoordDto(3.0, 3.0)))
        val fakePagingData: PagingData<City> = PagingData.from(cityList)
        val expectedFlow = flowOf(fakePagingData)
        `when`(mockCitiesRepository.getCitiesPaged()).thenReturn(expectedFlow)

        // Act
        val resultFlow = getCitiesPagedUseCase()

        // Assert
        resultFlow.test {
            val emittedPagingData = awaitItem()
            Assert.assertEquals(fakePagingData, emittedPagingData)
            // Actual pagination testing (load more, etc.) is outside the scope
            // of this specific unit test if the UseCase is just a pass-through.
            // It would be tested at the repository or UI level with PagingDataDiffer.
            awaitComplete()
        }
    }

    @Test
    @DisplayName("Data consistency: ensures emitted PagingData content matches expected")
    fun `Data consistency`() = testScope.runTest {
        // checking the content of the PagingData if it were more complex.
        // For a simple PagingData.from(), the equality check is usually enough.
        // Arrange
        val expectedCity = City(5,"Berlin", "Germany", CoordDto(4.0, 4.0))
        val cityList = listOf(expectedCity)
        val fakePagingData = PagingData.from(cityList)
        val expectedFlow = flowOf(fakePagingData)
        `when`(mockCitiesRepository.getCitiesPaged()).thenReturn(expectedFlow)

        // Act
        val resultFlow = getCitiesPagedUseCase()

        // Assert
        resultFlow.test {
            val emittedPagingData = awaitItem()
            Assert.assertEquals(fakePagingData, emittedPagingData)
            awaitComplete()
        }
    }

}