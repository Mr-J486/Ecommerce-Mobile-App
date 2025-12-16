package com.example.e_commercemobapp.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commercemobapp.databinding.ActivityLoginBinding
import com.example.e_commercemobapp.search.SearchActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        prefs = getSharedPreferences("login_prefs", MODE_PRIVATE)

        // ✅ Auto-login if Remember Me was checked
        val rememberMe = prefs.getBoolean("remember_me", false)
        if (rememberMe && auth.currentUser != null) {
            startActivity(Intent(this, SearchActivity::class.java))
            finish()
            return
        }

        // LOGIN BUTTON
        binding.loginBtn.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    // ✅ Save Remember Me choice
                    val remember = binding.rememberMeCheck.isChecked
                    prefs.edit()
                        .putBoolean("remember_me", remember)
                        .apply()

                    startActivity(Intent(this, SearchActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Login failed: ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }

        // GO TO REGISTER
        binding.goToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // FORGOT PASSWORD
        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }
}

