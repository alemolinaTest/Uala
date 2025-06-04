package com.amolina.presentation.ui.components.maps

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.amolina.domain.model.City
import com.amolina.presentation.ui.viewmodel.ICitiesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
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

    @Test
    fun showsSelectCityMessage_whenNoCitySelected() {
        val viewModel = createFakeViewModel(selectedCity = MutableStateFlow(null))

        composeTestRule.setContent {
            LandscapeCitiesMapScreen(
                viewModel = viewModel,
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
    ): ICitiesViewModel {
        val viewModel = mock<ICitiesViewModel>()

        whenever(viewModel.showFavouritesOnly).thenReturn(showFavouritesOnly)
        whenever(viewModel.selectedCity).thenReturn(selectedCity)
        whenever(viewModel.searchQuery).thenReturn(MutableStateFlow(""))
        whenever(viewModel.pagedCities).thenReturn(flowOf(PagingData.empty()))

        // no-op methods
        doNothing().whenever(viewModel).updateSearchQuery(org.mockito.kotlin.any())
        doNothing().whenever(viewModel).toggleShowFavourites()
        doNothing().whenever(viewModel).toggleFavourite(org.mockito.kotlin.any())

        return viewModel
    }
}
