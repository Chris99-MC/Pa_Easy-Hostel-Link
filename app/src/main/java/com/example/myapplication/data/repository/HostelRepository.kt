package com.example.myapplication.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.example.myapplication.data.model.Hostel

class HostelRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Register a new hostel
    fun addHostel(
        hostelName: String,
        location: String,
        description: String,
        totalRooms: Int,
        onResult: (Result<String>) -> Unit
    ) {

        val ownerId = auth.currentUser?.uid

        if (ownerId == null) {
            onResult(Result.failure(Exception("Please log in first.")))
            return
        }

        if (hostelName.isBlank() || location.isBlank() || totalRooms <= 0) {
            onResult(Result.failure(Exception("Please provide valid hostel details.")))
            return
        }

        val hostelRef = db.collection("hostels").document()

        val hostel = hashMapOf(
            "hostelId" to hostelRef.id,
            "ownerId" to ownerId,
            "hostelName" to hostelName.trim(),
            "location" to location.trim(),
            "description" to description.trim(),
            "totalRooms" to totalRooms,
            "createdAt" to FieldValue.serverTimestamp()
        )

        hostelRef.set(hostel)
            .addOnSuccessListener {
                onResult(Result.success(hostelRef.id))
            }
            .addOnFailureListener { exception ->
                onResult(Result.failure(exception))
            }
    }

    // Retrieve hostels belonging to the logged-in landlord
    fun getMyHostels(
        onResult: (Result<List<Hostel>>) -> Unit
    ) {

        val ownerId = auth.currentUser?.uid

        if (ownerId == null) {
            onResult(Result.failure(Exception("Please log in first.")))
            return
        }

        db.collection("hostels")
            .whereEqualTo("ownerId", ownerId)
            .get()
            .addOnSuccessListener { snapshot ->

                val hostels = snapshot.documents.mapNotNull { document ->
                    document.toObject(Hostel::class.java)
                        ?.copy(hostelId = document.id)
                }

                onResult(Result.success(hostels))
            }
            .addOnFailureListener { exception ->
                onResult(Result.failure(exception))
            }
    }

    // Update hostel information
    fun updateHostel(
        hostelId: String,
        hostelName: String,
        location: String,
        description: String,
        totalRooms: Int,
        onResult: (Result<Unit>) -> Unit
    ) {

        val ownerId = auth.currentUser?.uid

        if (ownerId == null) {
            onResult(Result.failure(Exception("Please log in first.")))
            return
        }

        if (hostelName.isBlank() || location.isBlank() || totalRooms <= 0) {
            onResult(Result.failure(Exception("Please provide valid hostel details.")))
            return
        }

        val updates = mapOf(
            "hostelName" to hostelName.trim(),
            "location" to location.trim(),
            "description" to description.trim(),
            "totalRooms" to totalRooms
        )

        db.collection("hostels")
            .document(hostelId)
            .update(updates)
            .addOnSuccessListener {
                onResult(Result.success(Unit))
            }
            .addOnFailureListener { exception ->
                onResult(Result.failure(exception))
            }
    }
}
