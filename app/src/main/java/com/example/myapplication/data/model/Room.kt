package com.example.myapplication.data.model

import com.google.firebase.Timestamp

data class Room(
    val roomId: String = "",
    val hostelId: String = "",
    val ownerId: String = "",
    val roomNumber: String = "",
    val capacity: Int = 1,
    val occupiedCount: Int = 0,
    val monthlyRent: Double = 0.0,
    val status: String = "available",
    val createdAt: Timestamp? = null
)
