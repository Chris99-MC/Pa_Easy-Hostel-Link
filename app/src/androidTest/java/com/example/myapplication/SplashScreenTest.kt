package com.example.myapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSplashScreenRenders() {
        var finished = false
        composeTestRule.setContent {
            MyApplicationTheme {
                SplashScreen(onSplashFinished = { finished = true })
            }
        }

        composeTestRule.onNodeWithText("Student Living, Simplified.").assertIsDisplayed()
        composeTestRule.onNodeWithText("v1.0.4").assertIsDisplayed()
        composeTestRule.onNodeWithText("CONCIERGE ENVIRONMENT READY.").assertIsDisplayed()
    }
}
