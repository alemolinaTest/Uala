package com.amolina.presentation.ui.components.maps

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.amolina.domain.model.City
import com.amolina.domain.model.CoordDto
import com.amolina.domain.util.Resource
import com.amolina.presentation.R
import com.amolina.presentation.ui.viewmodel.ICitiesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(AndroidJUnit4::class)
class LandscapeCitiesMapScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val dummyCity = City(
        id = 1,
        name = "Paris",
        countryCode = "FR",
        isFavourite = true,
        coord = CoordDto(48.8566, 2.3522)
    )

    @Test
    fun showsSelectCityMessage_whenNoCitySelected() {
        val viewModel = createFakeViewModel(selectedCity = MutableStateFlow(null))

        composeTestRule.setContent {
            LandscapeCitiesMapScreen(
                viewModel = viewModel,
                usePaging = false,
                onInfoClicked = {}
            )
        }

        composeTestRule.onNodeWithText("Select a city from the list")
            .assertIsDisplayed()
    }

    // Helper: Create fake ViewModel
    private fun createFakeViewModel(
        showFavouritesOnly: MutableStateFlow<Boolean> = MutableStateFlow(false),
        selectedCity: MutableStateFlow<City?> = MutableStateFlow(null),
        citiesState: MutableStateFlow<Resource<List<City>>> = MutableStateFlow(Resource.Loading),
        searchResults: MutableStateFlow<List<City>> = MutableStateFlow(emptyList())
    ): ICitiesViewModel {
        val viewModel = mock<ICitiesViewModel>()

        whenever(viewModel.showFavouritesOnly).thenReturn(showFavouritesOnly)
        whenever(viewModel.selectedCity).thenReturn(selectedCity)
        whenever(viewModel.citiesState).thenReturn(citiesState)
        whenever(viewModel.searchResults).thenReturn(searchResults)
        whenever(viewModel.searchQuery).thenReturn(MutableStateFlow(""))

        // no-op methods
        doNothing().whenever(viewModel).updateSearchQuery(org.mockito.kotlin.any())
        doNothing().whenever(viewModel).fetchCities()
        doNothing().whenever(viewModel).toggleShowFavourites()
        doNothing().whenever(viewModel).toggleFavourite(org.mockito.kotlin.any())

        return viewModel
    }
}
