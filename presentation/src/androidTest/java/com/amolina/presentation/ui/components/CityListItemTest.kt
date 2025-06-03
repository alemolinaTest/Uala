package com.amolina.presentation.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.amolina.domain.model.City
import com.amolina.domain.model.CoordDto
import org.junit.Rule
import org.junit.Test

class CityListItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cityListItem_displaysCityNameAndCountry() {
        val city = City(
            id = 1,
            name = "Paris",
            countryCode = "FR",
            CoordDto(1.0, 1.0),
            isFavourite = false
        )

        composeTestRule.setContent {
            CityListItem(
                city = city,
                onToggleFavourite = {},
                onCityClicked = {},
                onInfoClicked = {}
            )
        }

        composeTestRule
            .onNodeWithText("${city.name}, ${city.countryCode}")
            .assertExists()
    }

    @Test
    fun cityListItem_clickCard_triggersOnCityClicked() {
        val city = City(
            id = 1,
            name = "Paris",
            countryCode = "FR",
            CoordDto(1.0, 1.0),
            isFavourite = false
        )
        var clicked = false

        composeTestRule.setContent {
            CityListItem(
                city = city,
                onToggleFavourite = {},
                onCityClicked = { clicked = true },
                onInfoClicked = {}
            )
        }

        composeTestRule
            .onNodeWithTag("CityCard")
            .performClick()

        assert(clicked)
    }

    @Test
    fun cityListItem_clickInfoIcon_triggersOnInfoClicked() {
        val city = City(
            id = 1,
            name = "Paris",
            countryCode = "FR",
            CoordDto(1.0, 1.0),
            isFavourite = false
        )
        var infoClicked = false

        composeTestRule.setContent {
            CityListItem(
                city = city,
                onToggleFavourite = {},
                onCityClicked = {},
                onInfoClicked = { infoClicked = true }
            )
        }

        composeTestRule
            .onNodeWithContentDescription("City Info")
            .performClick()

        assert(infoClicked)
    }

    @Test
    fun cityListItem_clickFavouriteIcon_triggersOnToggleFavourite() {
        val city = City(
            id = 1,
            name = "Paris",
            countryCode = "FR",
            CoordDto(1.0, 1.0),
            isFavourite = false
        )
        var toggleClicked = false

        composeTestRule.setContent {
            CityListItem(
                city = city,
                onToggleFavourite = { toggleClicked = true },
                onCityClicked = {},
                onInfoClicked = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Favourite")
            .performClick()

        assert(toggleClicked)
    }

    @Test
    fun cityListItem_showsFilledStarWhenFavourite() {
        val city =
            City(id = 1, name = "Paris", countryCode = "FR", CoordDto(1.0, 1.0), isFavourite = true)

        composeTestRule.setContent {
            CityListItem(
                city = city,
                onToggleFavourite = {},
                onCityClicked = {},
                onInfoClicked = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Unfavourite")
            .assertExists()
    }

    @Test
    fun cityListItem_showsOutlinedStarWhenNotFavourite() {
        val city = City(
            id = 1,
            name = "Paris",
            countryCode = "FR",
            CoordDto(1.0, 1.0),
            isFavourite = false
        )

        composeTestRule.setContent {
            CityListItem(
                city = city,
                onToggleFavourite = {},
                onCityClicked = {},
                onInfoClicked = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Favourite")
            .assertExists()
    }
}
