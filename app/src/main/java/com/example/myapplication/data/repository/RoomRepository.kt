package com.example.myapplication.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.example.myapplication.data.model.Room

class RoomRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Add a room to a hostel
    fun addRoom(
        hostelId: String,
        roomNumber: String,
        capacity: Int,
        monthlyRent: Double,
        onResult: (Result<String>) -> Unit
    ) {

        val ownerId = auth.currentUser?.uid

        if (ownerId == null) {
            onResult(Result.failure(Exception("Please log in first.")))
            return
        }

        if (roomNumber.isBlank() || capacity <= 0 || monthlyRent < 0) {
            onResult(Result.failure(Exception("Please provide valid room details.")))
            return
        }

        val roomRef = db.collection("rooms").document()

        val room = hashMapOf(
            "roomId" to roomRef.id,
            "hostelId" to hostelId,
            "ownerId" to ownerId,
            "roomNumber" to roomNumber.trim(),
            "capacity" to capacity,
            "occupiedCount" to 0,
            "monthlyRent" to monthlyRent,
            "status" to "available",
            "createdAt" to FieldValue.serverTimestamp()
        )

        // Verify that the hostel belongs to the logged-in landlord
        db.collection("hostels")
            .document(hostelId)
            .get()
            .addOnSuccessListener { hostelDocument ->

                if (!hostelDocument.exists() ||
                    hostelDocument.getString("ownerId") != ownerId
                ) {
                    onResult(
                        Result.failure(Exception("You cannot add rooms to this hostel."))
                    )
                    return@addOnSuccessListener
                }

                roomRef.set(room)
                    .addOnSuccessListener {
                        onResult(Result.success(roomRef.id))
                    }
                    .addOnFailureListener { exception ->
                        onResult(Result.failure(exception))
                    }
            }
            .addOnFailureListener { exception ->
                onResult(Result.failure(exception))
            }
    }

    // Retrieve rooms belonging to a particular hostel
    fun getRooms(
        hostelId: String,
        onResult: (Result<List<Room>>) -> Unit
    ) {

        if (auth.currentUser == null) {
            onResult(Result.failure(Exception("Please log in first.")))
            return
        }

        db.collection("rooms")
            .whereEqualTo("hostelId", hostelId)
            .get()
            .addOnSuccessListener { snapshot ->

                val rooms = snapshot.documents.mapNotNull { document ->
                    document.toObject(Room::class.java)
                        ?.copy(roomId = document.id)
                }

                onResult(Result.success(rooms))
            }
            .addOnFailureListener { exception ->
                onResult(Result.failure(exception))
            }
    }

    // Update room details without changing occupancy
    fun updateRoom(
        roomId: String,
        roomNumber: String,
        capacity: Int,
        monthlyRent: Double,
        onResult: (Result<Unit>) -> Unit
    ) {

        val ownerId = auth.currentUser?.uid

        if (ownerId == null) {
            onResult(Result.failure(Exception("Please log in first.")))
            return
        }

        if (roomNumber.isBlank() || capacity <= 0 || monthlyRent < 0) {
            onResult(Result.failure(Exception("Please provide valid room details.")))
            return
        }

        db.collection("rooms")
            .document(roomId)
            .update(
                mapOf(
                    "roomNumber" to roomNumber.trim(),
                    "capacity" to capacity,
                    "monthlyRent" to monthlyRent
                )
            )
            .addOnSuccessListener {
                onResult(Result.success(Unit))
            }
            .addOnFailureListener { exception ->
                onResult(Result.failure(exception))
            }
    }
}
