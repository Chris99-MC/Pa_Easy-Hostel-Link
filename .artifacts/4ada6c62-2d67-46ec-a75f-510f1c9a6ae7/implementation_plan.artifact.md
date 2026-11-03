# Implementation Plan - Firebase Backend Integration

This plan outlines a safe, step-by-step approach to integrating **Firebase (Authentication & Cloud Firestore)** into the **Pa_Easy Hostel Link** application, addressing the gaps identified in `CORRECTIONS.docx` without introducing regressions.

## User Review Required

> [!IMPORTANT]
> **Firebase Project Configuration**: Before starting Phase 1, ensure you have created a Firebase project in the Firebase Console, enabled **Email/Password Authentication**, and downloaded your `google-services.json` file into the `app/` directory.

## Open Questions

- Should user roles (`Student` vs `Landlord`) be stored in Firestore user profile documents or Firebase Auth Custom Claims? (Recommended: Firestore user profiles for simplicity and flexibility).

## Proposed Changes

### Phase 1: Firebase Dependencies & Setup
- [ ] Add Firebase BoM and dependencies (`firebase-auth-ktx`, `firebase-firestore-ktx`) to [build.gradle.kts](file:///C:/Users/CHRIS/AndroidStudioProjects/Pa_Easy-Hostel-Link/Pa_Easy-Hostel-Link/app/build.gradle.kts).
- [ ] Ensure `google-services` plugin is configured.

### Phase 2: Firebase Authentication Integration
- [ ] Implement Firebase Auth repository/manager for sign-in and registration.
- [ ] Update [LoginScreen.kt](file:///C:/Users/CHRIS/AndroidStudioProjects/Pa_Easy-Hostel-Link/Pa_Easy-Hostel-Link/app/src/main/java/com/example/myapplication/LoginScreen.kt) and [MainActivity.kt](file:///C:/Users/CHRIS/AndroidStudioProjects/Pa_Easy-Hostel-Link/Pa_Easy-Hostel-Link/app/src/main/java/com/example/myapplication/MainActivity.kt) to authenticate against Firebase Auth.

### Phase 3: Cloud Firestore Data Models & Repositories
- [ ] Create Firestore collections and data models for Hostels, Rooms, and Applications.
- [ ] Replace in-memory sample lists in Landlord and Student screens with Firestore real-time queries.

### Phase 4: Landlord & Student Workflow Synchronization
- [ ] Sync Landlord room addition and application approval/allocation with Firestore.
- [ ] Sync Student application submission and status tracking with Firestore.

## Verification Plan

### Automated Tests
- Run Gradle builds (`app:assembleDebug`) and UI tests (`app:assembleDebugAndroidTest`) after each phase.

### Manual Verification
- Deploy to an emulator/device and test real authentication and Firestore data reads/writes.
