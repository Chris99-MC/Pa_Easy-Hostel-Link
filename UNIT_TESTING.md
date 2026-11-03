# Unit & UI Testing Guide - Pa_Easy Hostel Link

This document provides complete documentation for the Jetpack Compose unit and UI test suite implemented for the **Pa_Easy Hostel Link** Android application.

---

## 1. Test Architecture & Design Principles

The test suite is built using **Jetpack Compose UI Testing (`androidx.compose.ui.test.junit4`)** and **AndroidJUnit4**. 

To maintain high code quality, modularity, and readability, **every screen and core visual component has its own dedicated test file** under `app/src/androidTest/java/com/example/myapplication/`.

---

## 2. Test Files Breakdown

| File Name | Target Component / Screen | Description & Coverage |
| :--- | :--- | :--- |
| **[SplashScreenTest.kt](file:///C:/Users/CHRIS/AndroidStudioProjects/Pa_Easy-Hostel-Link/Pa_Easy-Hostel-Link/app/src/androidTest/java/com/example/myapplication/SplashScreenTest.kt)** | `SplashScreen` | Validates splash screen branding elements (`Student Living, Simplified.`), version info (`v1.0.4`), concierge status banner, and completion callback execution. |
| **[LoginScreenTest.kt](file:///C:/Users/CHRIS/AndroidStudioProjects/Pa_Easy-Hostel-Link/Pa_Easy-Hostel-Link/app/src/androidTest/java/com/example/myapplication/LoginScreenTest.kt)** | `LoginScreen` | Tests multi-role authentication (Student vs. Landlord tab selection), login success callbacks, and switching between "Sign In" and "Register" form modes. |
| **[StudentHomeScreenTest.kt](file:///C:/Users/CHRIS/AndroidStudioProjects/Pa_Easy-Hostel-Link/Pa_Easy-Hostel-Link/app/src/androidTest/java/com/example/myapplication/StudentHomeScreenTest.kt)** | `StudentHomeScreen` | Tests student dashboard layout, top bar branding, accommodation lists, and search query input filtering UX. |
| **[LandlordDashboardScreenTest.kt](file:///C:/Users/CHRIS/AndroidStudioProjects/Pa_Easy-Hostel-Link/Pa_Easy-Hostel-Link/app/src/androidTest/java/com/example/myapplication/LandlordDashboardScreenTest.kt)** | `LandlordDashboardScreen` | Validates Landlord Portal header rendering, overview stats, and property management tabs. |
| **[HostelLogoTest.kt](file:///C:/Users/CHRIS/AndroidStudioProjects/Pa_Easy-Hostel-Link/Pa_Easy-Hostel-Link/app/src/androidTest/java/com/example/myapplication/HostelLogoTest.kt)** | `HostelLogo` | Tests custom Canvas-based brand logo rendering across different dimensions and configurations. |
| **[MainActivityNavigationTest.kt](file:///C:/Users/CHRIS/AndroidStudioProjects/Pa_Easy-Hostel-Link/Pa_Easy-Hostel-Link/app/src/androidTest/java/com/example/myapplication/MainActivityNavigationTest.kt)** | `MainActivity` | Integration test verifying activity launch, edge-to-edge configuration, and initial splash navigation routing. |

---

## 3. Detailed Test Cases

### SplashScreenTest
```kotlin
@Test
fun testSplashScreenRenders() {
    // Verifies splash screen UI components are properly displayed
    composeTestRule.onNodeWithText("Student Living, Simplified.").assertIsDisplayed()
    composeTestRule.onNodeWithText("v1.0.4").assertIsDisplayed()
    composeTestRule.onNodeWithText("CONCIERGE ENVIRONMENT READY.").assertIsDisplayed()
}
```

### LoginScreenTest
- **`testLoginScreenStudentFlow`**: Asserts student login prompt and invokes `LOGIN` button.
- **`testLoginScreenLandlordFlow`**: Clicks the Landlord tab and asserts landlord authentication flow.
- **`testLoginScreenRegistrationMode`**: Switches to registration mode and asserts appropriate form fields.

### StudentHomeScreenTest
- **`testStudentHomeScreenRendersAndInteracts`**: Asserts header texts (`Pa_Easy Hostel Link`, `Student Accommodations`) and tests search box text input.

### LandlordDashboardScreenTest
- **`testLandlordDashboardScreenRenders`**: Asserts presence of `Landlord Portal`.

---

## 4. How to Run the Tests

### Option A: Using Android Studio
1. Open the project in Android Studio.
2. In the Project pane, expand `app/src/androidTest/java/com/example/myapplication`.
3. Right-click any test file (e.g., `LoginScreenTest.kt`) and select **Run 'LoginScreenTest'**, or right-click the package to run all tests.

### Option B: Using Gradle (Command Line)
To execute all instrumented UI tests on a connected emulator or device:
```bash
./gradlew connectedAndroidTest
```
