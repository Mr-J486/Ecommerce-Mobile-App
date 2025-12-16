
package com.example.e_commercemobapp.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commercemobapp.databinding.ActivityLoginBinding
import com.example.e_commercemobapp.search.SearchActivity
import com.example.e_commercemobapp.admin.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var prefs: SharedPreferences
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        prefs = getSharedPreferences("login_prefs", MODE_PRIVATE)

        // =========================
        // LOGIN BUTTON
        // =========================
        binding.loginBtn.setOnClickListener {

            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Enter email and password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    // Save Remember Me choice (optional)
                    val remember = binding.rememberMeCheck.isChecked
                    prefs.edit()
                        .putBoolean("remember_me", remember)
                        .apply()

                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        redirectByRole(uid)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Login failed: ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }

        // =========================
        // NAVIGATION
        // =========================
        binding.goToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    // =========================
    // ROLE-BASED REDIRECT
    // =========================
    private fun redirectByRole(uid: String) {
        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { snapshot ->

                val isAdmin = snapshot.getBoolean("admin") ?: false

                if (isAdmin) {
                    // ✅ ADMIN
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    // ✅ NORMAL USER
                    startActivity(Intent(this, SearchActivity::class.java))
                }

                finish()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Failed to load user data",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}
