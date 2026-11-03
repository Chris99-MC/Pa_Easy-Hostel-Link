package com.example.myapplication.data.model

import com.google.firebase.Timestamp

data class Hostel(
    val hostelId: String = "",
    val ownerId: String = "",
    val hostelName: String = "",
    val location: String = "",
    val description: String = "",
    val totalRooms: Int = 0,
    val createdAt: Timestamp? = null
)
