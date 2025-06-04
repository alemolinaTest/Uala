package com.amolina.presentation.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.amolina.domain.model.City
import com.amolina.domain.model.CoordDto
import com.amolina.domain.util.Resource
import com.amolina.presentation.ui.viewmodel.ICitiesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import com.amolina.presentation.R


class CitiesListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val dummyCity = City(
        id = 1,
        name = "Paris",
        countryCode = "FR",
        isFavourite = true,
        coord = CoordDto(48.8566, 2.3522)
    )

    @Test
    fun citiesListScreen_showsTopAppBarTitle() {
        val viewModel = createFakeViewModel()

        composeTestRule.setContent {
            CitiesListScreen(
                viewModel = viewModel,
                onCityClicked = {},
                onInfoClicked = {}
            )
        }

        composeTestRule.onNodeWithText("Cities App")
            .assertIsDisplayed()
    }

    @Test
    fun citiesListScreen_displaysLoader_whenLoading() {
        val viewModel = createFakeViewModel(
            citiesState = MutableStateFlow(Resource.Loading)
        )

        composeTestRule.setContent {
            CitiesListScreen(
                viewModel = viewModel,
                onCityClicked = {},
                onInfoClicked = {}
            )
        }

        composeTestRule.onNodeWithTag("CircularProgressIndicator")
            .assertExists()
    }

    @Test
    fun citiesListScreen_displaysError_whenError() {
        val viewModel = createFakeViewModel()
        val errorPagingFlow = Pager(
            config = PagingConfig(pageSize = 20)
        ) { ErrorPagingSource() }.flow

        whenever(viewModel.pagedCities).thenReturn(errorPagingFlow)

        composeTestRule.setContent {
            CitiesListScreen(
                viewModel = viewModel,
                onCityClicked = {},
                onInfoClicked = {}
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Error: Network Error")
            .assertIsDisplayed()
    }

    @Test
    fun citiesListScreen_displaysCityList_whenSuccess() {
        val viewModel = createFakeViewModel(
            citiesState = MutableStateFlow(Resource.Success(listOf(dummyCity))),
            searchResults = MutableStateFlow(listOf(dummyCity))
        )

        composeTestRule.setContent {
            CitiesListScreen(
                viewModel = viewModel,
                onCityClicked = {},
                onInfoClicked = {}
            )
        }
        composeTestRule.waitForIdle() // ensures Compose hierarchy is ready


        composeTestRule.onNodeWithText("${dummyCity.name}, ${dummyCity.countryCode}")
            .assertIsDisplayed()
    }

    // Helper: create fake ViewModel
    private fun createFakeViewModel(
        showFavouritesOnly: MutableStateFlow<Boolean> = MutableStateFlow(false),
        selectedCity: MutableStateFlow<City?> = MutableStateFlow(null),
        citiesState: MutableStateFlow<Resource<List<City>>> = MutableStateFlow(Resource.Loading),
        searchResults: MutableStateFlow<List<City>> = MutableStateFlow(emptyList()),
        searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    ): ICitiesViewModel {
        val viewModel = mock(ICitiesViewModel::class.java)

        // Return the passed-in MutableStateFlows
        whenever(viewModel.showFavouritesOnly).thenReturn(showFavouritesOnly)
        whenever(viewModel.selectedCity).thenReturn(selectedCity)
        whenever(viewModel.searchQuery).thenReturn(searchQuery)
        whenever(viewModel.pagedCities).thenReturn(flowOf(PagingData.from(listOf(dummyCity))))


        // Do-nothing methods
        doNothing().whenever(viewModel).updateSearchQuery(anyString())
        doNothing().whenever(viewModel).toggleShowFavourites()
        doNothing().whenever(viewModel).toggleFavourite(anyInt())

        return viewModel
    }
}
