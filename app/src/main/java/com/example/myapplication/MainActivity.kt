package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("splash") }

                    when (currentScreen) {
                        "splash" -> SplashScreen(
                            onSplashFinished = { currentScreen = "login" }
                        )
                        "login" -> LoginScreen(
                            onLoginSuccess = { role ->
                                currentScreen = if (role == "Landlord") "landlord_home" else "home"
                            }
                        )
                        "home" -> StudentHomeScreen(
                            onNavigateBack = { currentScreen = "login" }
                        )
                        "landlord_home" -> LandlordDashboardScreen(
                            onLogout = { currentScreen = "login" }
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Splash Screen", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun SplashScreenPreview() {
    MyApplicationTheme {
        SplashScreen(onSplashFinished = {})
    }
}

@Preview(name = "Login Screen", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun LoginScreenPreview() {
    MyApplicationTheme {
        LoginScreen(onLoginSuccess = {})
    }
}

@Preview(name = "Student Home Screen", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun StudentHomeScreenPreview() {
    MyApplicationTheme {
        StudentHomeScreen()
    }
}

@Preview(name = "Landlord Dashboard Screen", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun LandlordDashboardScreenPreview() {
    MyApplicationTheme {
        LandlordDashboardScreen()
    }
}
