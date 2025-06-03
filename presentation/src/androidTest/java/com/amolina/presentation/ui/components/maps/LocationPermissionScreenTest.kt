package com.amolina.presentation.ui.components.maps

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.amolina.presentation.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalPermissionsApi::class)
class LocationPermissionScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun locationPermissionScreen_showsGrantedText_whenPermissionsGranted() {
        // Arrange
        val permissionsState = mock<MultiplePermissionsState>()
        whenever(permissionsState.allPermissionsGranted).thenReturn(true)

        val expectedText = composeTestRule.activity.getString(
            R.string.location_permissions_granted_you_can_use_location_based_features
        )

        // Act
        composeTestRule.setContent {
            LocationPermissionScreen(locationPermissionsState = permissionsState)
        }

        // Assert
        composeTestRule.onNodeWithText(expectedText).assertIsDisplayed()
    }

    @Test
    fun locationPermissionScreen_requestsPermissions_whenNotGranted() {
        // Arrange
        val permissionsState = mock<MultiplePermissionsState>()
        whenever(permissionsState.allPermissionsGranted).thenReturn(false)

        val grantedText = composeTestRule.activity.getString(
            R.string.location_permissions_granted_you_can_use_location_based_features
        )

        // Act
        composeTestRule.setContent {
            LocationPermissionScreen(locationPermissionsState = permissionsState)
        }

        // Assert: granted text should NOT be displayed
        composeTestRule.onNodeWithText(grantedText).assertDoesNotExist()
    }
}
