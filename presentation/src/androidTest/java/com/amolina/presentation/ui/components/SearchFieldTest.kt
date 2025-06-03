package com.amolina.presentation.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchField_displaysQueryText() {
        val testQuery = "Test City"

        composeTestRule.setContent {
            SearchField(
                query = testQuery,
                onQueryChanged = {},
                onClearQuery = {}
            )
        }

        // Verify that the query is displayed in the TextField
        composeTestRule
            .onNodeWithText(testQuery)
            .assertExists()
    }

    @Test
    fun searchField_clearButtonIsVisibleWhenQueryNotEmpty() {
        val testQuery = "Test"

        composeTestRule.setContent {
            SearchField(
                query = testQuery,
                onQueryChanged = {},
                onClearQuery = {}
            )
        }

        // Check that the clear icon button is visible
        composeTestRule
            .onNodeWithContentDescription("Clear")
            .assertExists()
    }

    @Test
    fun searchField_clearButtonNotVisibleWhenQueryEmpty() {
        composeTestRule.setContent {
            SearchField(
                query = "",
                onQueryChanged = {},
                onClearQuery = {}
            )
        }

        // Check that the clear icon button does not exist
        composeTestRule
            .onNodeWithContentDescription("Clear")
            .assertDoesNotExist()
    }

    @Test
    fun searchField_clearButtonCallsOnClearQuery() {
        var clearCalled = false

        composeTestRule.setContent {
            SearchField(
                query = "Test",
                onQueryChanged = {},
                onClearQuery = { clearCalled = true }
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Clear")
            .performClick()

        assert(clearCalled)
    }
}
