package com.example.e_commercemobapp.repository

import com.example.e_commercemobapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // =========================
    // LOGIN
    // =========================
    fun login(
        email: String,
        password: String,
        callback: (Boolean, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                callback(true, null)
            }
            .addOnFailureListener { e ->
                callback(false, e.message)
            }
    }

    // =========================
    // REGISTER
    // =========================
    fun register(
        email: String,
        password: String,
        user: User,
        callback: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    callback(false, "User ID not found")
                    return@addOnSuccessListener
                }

                // Store User object directly (isAdmin will be saved correctly)
                db.collection("users")
                    .document(uid)
                    .set(user)
                    .addOnSuccessListener {
                        callback(true, null)
                    }
                    .addOnFailureListener { e ->
                        callback(false, e.message)
                    }
            }
            .addOnFailureListener { e ->
                callback(false, e.message)
            }
    }

    // =========================
    // RESET PASSWORD
    // =========================
    fun resetPassword(
        email: String,
        callback: (Boolean, String?) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                callback(true, null)
            }
            .addOnFailureListener { e ->
                callback(false, e.message)
            }
    }

    // =========================
    // LOGOUT
    // =========================
    fun logout() {
        auth.signOut()
    }
}
