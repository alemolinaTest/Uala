package com.amolina.presentation.ui.components.maps

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.amolina.domain.model.City
import com.amolina.domain.model.CoordDto
import com.amolina.presentation.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GoogleMapScreenTest {

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
    fun googleMapScreen_showsNoCitiesMessage_whenCitiesListIsEmpty() {
        composeTestRule.setContent {
            GoogleMapScreen(cities = emptyList())
        }

        composeTestRule
            .onNodeWithText("No cities available")
            .assertIsDisplayed()
    }
}
