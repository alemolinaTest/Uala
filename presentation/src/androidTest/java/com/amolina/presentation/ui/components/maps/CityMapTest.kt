package com.amolina.presentation.ui.components.maps

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.amolina.presentation.R
import org.junit.Rule
import org.junit.Test

class CityMapTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

//    private val dummyCities = listOf(
//        City(
//            id = 1,
//            name = "Paris",
//            countryCode = "FR",
//            isFavourite = true,
//            coord = CoordDto(48.8566, 2.3522)
//        )
//    )

//    @Test
//    fun cityMap_displaysMap_whenCitiesProvided() {
//        composeTestRule.setContent {
//            CityMap(
//                cities = dummyCities,
//                modifier = Modifier.testTag("CityMap")
//            )
//        }
//
//        // Assert the GoogleMap is displayed via the test tag
//        composeTestRule.onNodeWithTag("CityMap").assertExists()
//    }

    @Test
    fun cityMap_displaysNoCitiesMessage_whenCitiesEmpty() {
        composeTestRule.setContent {
            CityMap(
                cities = emptyList()
            )
        }

        // Assert that the "No cities available" message is displayed
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.no_cities_available)
        ).assertIsDisplayed()
    }
}
