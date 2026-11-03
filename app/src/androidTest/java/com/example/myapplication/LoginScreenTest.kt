package com.example.myapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoginScreenStudentFlow() {
        var successRole = ""
        composeTestRule.setContent {
            MyApplicationTheme {
                LoginScreen(onLoginSuccess = { role ->
                    successRole = role
                })
            }
        }

        composeTestRule.onNodeWithText("Student Accommodation Simplified").assertIsDisplayed()
        composeTestRule.onNodeWithText("LOGIN").assertIsDisplayed()
        composeTestRule.onNodeWithText("LOGIN").performClick()
        assert(successRole == "Student")
    }

    @Test
    fun testLoginScreenLandlordFlow() {
        var successRole = ""
        composeTestRule.setContent {
            MyApplicationTheme {
                LoginScreen(onLoginSuccess = { role ->
                    successRole = role
                })
            }
        }

        composeTestRule.onNodeWithText("Landlord").performClick()
        composeTestRule.onNodeWithText("LOGIN").performClick()
        assert(successRole == "Landlord")
    }

    @Test
    fun testLoginScreenRegistrationMode() {
        composeTestRule.setContent {
            MyApplicationTheme {
                LoginScreen()
            }
        }

        composeTestRule.onNodeWithText("Register").performClick()
        composeTestRule.onNodeWithText("New Account Registration").assertIsDisplayed()

        composeTestRule.onNodeWithText("Landlord").performClick()
        composeTestRule.onNodeWithText("Portfolio Manager / Landlord Name").assertIsDisplayed()
    }
}
