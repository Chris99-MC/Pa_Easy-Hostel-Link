# UI Testing Documentation - Pa_Easy Hostel Link

This document provides a comprehensive overview of the UI test suite implemented for the **Pa_Easy Hostel Link** Android application using Jetpack Compose UI Testing (`androidx.compose.ui.test`).

## Overview of Test Architecture

The UI test suite follows a modular, separated architecture where each screen and core component has its own dedicated test file. This ensures high maintainability, clear separation of concerns, and parallel test execution readiness.

## Test Files Summary

| Test File | Target Component | Description |
| :--- | :--- | :--- |
| [SplashScreenTest.kt](file:///C:/Users/CHRIS/AndroidStudioProjects/Pa_Easy-Hostel-Link/Pa_Easy-Hostel-Link/app/src/androidTest/java/com/example/myapplication/SplashScreenTest.kt) | `SplashScreen` | Verifies splash screen UI rendering (`StudentLiving, Simplified.`, version `v1.0.4`, concierge ready banner) and completion callback handling. |
| [LoginScreenTest.kt](file:///C:/Users/CHRIS/AndroidStudioProjects/Pa_Easy-Hostel-Link/Pa_Easy-Hostel-Link/app/src/androidTest/java/com/example/myapplication/LoginScreenTest.kt) | `LoginScreen` | Tests student sign-in flow, landlord sign-in flow, role tab switching (`Student` / `Landlord`), and registration mode form fields. |
| [StudentHomeScreenTest.kt](file:///C:/Users/CHRIS/AndroidStudioProjects/Pa_Easy-Hostel-Link/Pa_Easy-Hostel-Link/app/src/androidTest/java/com/example/myapplication/StudentHomeScreenTest.kt) | `StudentHomeScreen` | Tests student home screen rendering, top bar branding, accommodation lists, and search query input interactions. |
| [LandlordDashboardScreenTest.kt](file:///C:/Users/CHRIS/AndroidStudioProjects/Pa_Easy-Hostel-Link/Pa_Easy-Hostel-Link/app/src/androidTest/java/com/example/myapplication/LandlordDashboardScreenTest.kt) | `LandlordDashboardScreen` | Validates landlord portal overview, dashboard stats, and management tab components. |
| [HostelLogoTest.kt](file:///C:/Users/CHRIS/AndroidStudioProjects/Pa_Easy-Hostel-Link/Pa_Easy-Hostel-Link/app/src/androidTest/java/com/example/myapplication/HostelLogoTest.kt) | `HostelLogo` | Tests the custom Canvas-based brand logo rendering across different configurations. |
| [MainActivityNavigationTest.kt](file:///C:/Users/CHRIS/AndroidStudioProjects/Pa_Easy-Hostel-Link/Pa_Easy-Hostel-Link/app/src/androidTest/java/com/example/myapplication/MainActivityNavigationTest.kt) | `MainActivity` | Tests activity launch and initial route navigation flow. |

## Detailed Test Specifications

### 1. SplashScreenTest
- **Purpose**: Ensure the splash screen initializes correctly and displays brand elements.
- **Key assertions**:
  - `onNodeWithText("Student Living, Simplified.")` is displayed.
  - `onNodeWithText("v1.0.4")` version tag is visible.
  - `onNodeWithText("CONCIERGE ENVIRONMENT READY.")` banner is present.

### 2. LoginScreenTest
- **Purpose**: Validate multi-role authentication and registration switching.
- **Key test cases**:
  - `testLoginScreenStudentFlow`: Verifies student sign-in state and successful login invocation.
  - `testLoginScreenLandlordFlow`: Clicks the "Landlord" role tab and verifies login success for landlords.
  - `testLoginScreenRegistrationMode`: Switches to "Register" mode and checks dynamic form fields for both student and landlord registrations.

### 3. StudentHomeScreenTest
- **Purpose**: Verify student dashboard elements and search functionality.
- **Key interactions**:
  - Asserts branding elements (`Pa_Easy Hostel Link`, `Student Accommodations`).
  - Performs text input on the search bar to test filtering UX.

### 4. LandlordDashboardScreenTest
- **Purpose**: Ensure landlord management portal renders correctly.
- **Key assertions**:
  - Asserts existence of `Landlord Portal` header and dashboard components.

### 5. HostelLogoTest
- **Purpose**: Verify custom vector logo rendering.
- **Key assertions**:
  - Renders `HostelLogo` inside a Compose theme container without runtime layout exceptions.

### 6. MainActivityNavigationTest
- **Purpose**: Integration test for activity startup.
- **Key assertions**:
  - Verifies app launches into the initial splash state.

## How to Execute the UI Tests

### From Android Studio
1. Open the Project in Android Studio.
2. Navigate to `app/src/androidTest/java/com/example/myapplication/`.
3. Right-click any test file (or the `myapplication` package) and select **Run 'Tests in ...'**.
4. View results in the Run tool window.

### From Terminal (Gradle)
Run the following command to assemble and execute instrumented tests on a connected emulator or physical device:
```bash
./gradlew connectedAndroidTest
```
