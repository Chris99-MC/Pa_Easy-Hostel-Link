
package com.example.myapplication.data.model

import com.google.firebase.Timestamp

data class UserProfile(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",

    // Student information
    val studentId: String = "",
    val program: String = "",

    // Landlord information
    val propertyName: String = "",
    val managedRooms: String = "",

    val role: String = "",
    val accountStatus: String = "",
    val createdAt: Timestamp? = null
)
