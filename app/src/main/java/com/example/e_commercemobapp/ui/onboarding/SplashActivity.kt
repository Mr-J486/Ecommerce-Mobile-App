
package com.example.e_commercemobapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commercemobapp.search.SearchActivity
import com.example.e_commercemobapp.admin.MainActivity
import com.example.e_commercemobapp.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("login_prefs", MODE_PRIVATE)
        val rememberMe = prefs.getBoolean("remember_me", false)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        // ❌ User exists but Remember Me is OFF → force logout
        if (user != null && !rememberMe) {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // ❌ Not logged in
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // ✅ Logged in + Remember Me ON → check role
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { snapshot ->

                val isAdmin = snapshot.getBoolean("admin") ?: false

                if (isAdmin) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    startActivity(Intent(this, SearchActivity::class.java))
                }

                finish()
            }
            .addOnFailureListener {
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
    }
}
