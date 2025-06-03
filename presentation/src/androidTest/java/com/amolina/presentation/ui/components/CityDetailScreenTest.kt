package com.amolina.presentation.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.amolina.domain.model.City
import com.amolina.domain.model.CoordDto
import org.junit.Rule
import org.junit.Test

class CityDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val dummyCity = City(
        id = 1,
        name = "Paris",
        countryCode = "FR",
        isFavourite = true,
        coord = CoordDto(latitude = 48.8566, longitude = 2.3522)
    )

    @Test
    fun cityDetailScreen_displaysCityDetails() {
        composeTestRule.setContent {
            CityDetailScreen(
                city = dummyCity,
                onToggleFavourite = {},
                onBackPressed = {}
            )
        }

        composeTestRule.onNodeWithText("${dummyCity.name}, ${dummyCity.countryCode}")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("City: ${dummyCity.name}")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Country Code: ${dummyCity.countryCode}")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Latitude: ${dummyCity.coord.latitude}")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Longitude: ${dummyCity.coord.longitude}")
            .assertIsDisplayed()
    }

    @Test
    fun cityDetailScreen_backButtonCallsOnBackPressed() {
        var backPressed = false

        composeTestRule.setContent {
            CityDetailScreen(
                city = dummyCity,
                onToggleFavourite = {},
                onBackPressed = { backPressed = true }
            )
        }

        composeTestRule.onNodeWithContentDescription("Back")
            .performClick()

        assert(backPressed)
    }

    @Test
    fun cityDetailScreen_toggleFavouriteCallsOnToggleFavourite() {
        var toggledId: Int? = null

        composeTestRule.setContent {
            CityDetailScreen(
                city = dummyCity,
                onToggleFavourite = { id -> toggledId = id },
                onBackPressed = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Unfavourite")
            .performClick()

        assert(toggledId == dummyCity.id)
    }

    @Test
    fun cityDetailScreen_showsStarWhenFavourite() {
        composeTestRule.setContent {
            CityDetailScreen(
                city = dummyCity.copy(isFavourite = true),
                onToggleFavourite = {},
                onBackPressed = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Unfavourite")
            .assertExists()
    }

    @Test
    fun cityDetailScreen_showsStarBorderWhenNotFavourite() {
        composeTestRule.setContent {
            CityDetailScreen(
                city = dummyCity.copy(isFavourite = false),
                onToggleFavourite = {},
                onBackPressed = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Favourite")
            .assertExists()
    }
}
