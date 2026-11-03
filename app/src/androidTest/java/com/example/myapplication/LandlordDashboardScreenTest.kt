package com.example.myapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LandlordDashboardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLandlordDashboardScreenRenders() {
        composeTestRule.setContent {
            MyApplicationTheme {
                LandlordDashboardScreen()
            }
        }

        composeTestRule.onNodeWithText("Landlord Portal").assertIsDisplayed()
    }
}
