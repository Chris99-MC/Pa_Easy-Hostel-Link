package com.example.myapplication.data.repository

import com.example.myapplication.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // REGISTER USER
    fun registerUser(
        fullName: String,
        email: String,
        password: String,
        role: String,
        studentId: String = "",
        program: String = "",
        phoneNumber: String = "",
        propertyName: String = "",
        managedRooms: String = "",
        onResult: (Result<UserProfile>) -> Unit
    ) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->

                val firebaseUser = authResult.user

                if (firebaseUser == null) {
                    onResult(
                        Result.failure(
                            Exception("User registration failed.")
                        )
                    )
                    return@addOnSuccessListener
                }

                val uid = firebaseUser.uid

                val accountStatus =
                    if (role == "landlord") "pending"
                    else "active"

                val profile = hashMapOf(
                    "uid" to uid,
                    "fullName" to fullName,
                    "email" to email,
                    "phoneNumber" to "",
                    "studentId" to studentId,
                    "program" to program,
                    "propertyName" to propertyName,
                    "managedRooms" to managedRooms,
                    "role" to role,
                    "accountStatus" to accountStatus,
                    "createdAt" to FieldValue.serverTimestamp()
                )
                db.collection("users")
                    .document(uid)
                    .set(profile)
                    .addOnSuccessListener {

                        val userProfile = UserProfile(
                            uid = uid,
                            fullName = fullName,
                            email = email,
                            phoneNumber = phoneNumber,
                            role = role,
                            accountStatus = accountStatus
                        )

                        onResult(Result.success(userProfile))
                    }
                    .addOnFailureListener { exception ->
                        onResult(Result.failure(exception))
                    }
            }
            .addOnFailureListener { exception ->
                onResult(Result.failure(exception))
            }
    }

    // LOGIN USER
    fun loginUser(
        email: String,
        password: String,
        onResult: (Result<UserProfile>) -> Unit
    ) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                val uid = auth.currentUser?.uid

                if (uid == null) {
                    onResult(
                        Result.failure(
                            Exception("Unable to identify user.")
                        )
                    )
                    return@addOnSuccessListener
                }

                db.collection("users")
                    .document(uid)
                    .get()
                    .addOnSuccessListener { document ->

                        if (!document.exists()) {
                            auth.signOut()

                            onResult(
                                Result.failure(
                                    Exception("User profile not found.")
                                )
                            )
                            return@addOnSuccessListener
                        }

                        val userProfile = UserProfile(
                            uid = uid,
                            fullName = document.getString("fullName") ?: "",
                            email = document.getString("email") ?: "",
                            phoneNumber = document.getString("phoneNumber") ?: "",
                            role = document.getString("role") ?: "",
                            accountStatus = document.getString("accountStatus") ?: ""
                        )

                        if (userProfile.accountStatus != "active") {
                            auth.signOut()

                            onResult(
                                Result.failure(
                                    Exception("Your account is awaiting approval.")
                                )
                            )
                            return@addOnSuccessListener
                        }

                        onResult(Result.success(userProfile))
                    }
                    .addOnFailureListener { exception ->
                        auth.signOut()
                        onResult(Result.failure(exception))
                    }
            }
            .addOnFailureListener { exception ->
                onResult(Result.failure(exception))
            }
    }

    // LOGOUT USER
    fun logoutUser() {
        auth.signOut()
    }

    // CHECK CURRENT USER
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
