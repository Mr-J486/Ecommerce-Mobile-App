//package com.example.e_commercemobapp.repository
//
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.example.e_commercemobapp.model.User
//
//class AuthRepository {
//
//    private val auth = FirebaseAuth.getInstance()
//    private val db = FirebaseFirestore.getInstance()
//
//    // REGISTER USER
//    fun registerUser(email: String, password: String, user: User, callback: (Boolean, String?) -> Unit) {
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnSuccessListener {
//                val uid = auth.uid!!
//                db.collection("users").document(uid).set(user)
//                callback(true, null)
//            }
//            .addOnFailureListener { e ->
//                callback(false, e.message)
//            }
//    }
//
//    // LOGIN USER
//    fun loginUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnSuccessListener { callback(true, null) }
//            .addOnFailureListener { e -> callback(false, e.message) }
//    }
//
//    // RESET PASSWORD
//    fun resetPassword(email: String, callback: (Boolean, String?) -> Unit) {
//        auth.sendPasswordResetEmail(email)
//            .addOnSuccessListener { callback(true, null) }
//            .addOnFailureListener { e -> callback(false, e.message) }
//    }
//
//    fun logout() {
//        auth.signOut()
//    }
//}
package com.example.e_commercemobapp.repository

import com.example.e_commercemobapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

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
    // REGISTER (EMAIL UNIQUE)
    // =========================
    fun register(
        email: String,
        password: String,
        user: User,
        callback: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener

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
}
