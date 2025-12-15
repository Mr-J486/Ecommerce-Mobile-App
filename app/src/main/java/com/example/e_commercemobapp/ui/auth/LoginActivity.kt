//package com.example.e_commercemobapp.ui.auth
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.e_commercemobapp.databinding.ActivityLoginBinding
//import com.example.e_commercemobapp.search.SearchActivity
//
//class LoginActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityLoginBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // LOGIN BUTTON
//        binding.loginBtn.setOnClickListener {
//            val email = binding.emailInput.text.toString()
//            val password = binding.passwordInput.text.toString()
//
//            if (email.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // ✅ Login success → go directly to shopping
//            startActivity(Intent(this, SearchActivity::class.java))
//            finish()
//        }
//
//        // SIGN UP
//        binding.goToRegister.setOnClickListener {
//            startActivity(Intent(this, RegisterActivity::class.java))
//        }
//
//        // FORGOT PASSWORD
//        binding.forgotPassword.setOnClickListener {
//            startActivity(Intent(this, ForgotPasswordActivity::class.java))
//        }
//    }
//}
package com.example.e_commercemobapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commercemobapp.databinding.ActivityLoginBinding
import com.example.e_commercemobapp.search.SearchActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // 🔐 FORCE fresh login (VERY IMPORTANT)


        binding.loginBtn.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ REAL AUTH
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
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

        binding.goToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }
}

