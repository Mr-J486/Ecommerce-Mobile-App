package com.example.e_commercemobapp.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commercemobapp.databinding.ActivityLoginBinding
import com.example.e_commercemobapp.ui.home.HomeActivity
import com.example.e_commercemobapp.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadRememberMe()

        // Login Button
        binding.loginBtn.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            saveRememberMe(email, password)

            viewModel.login(email, password)
        }

        viewModel.authStatus.observe(this) { success ->
            if (success == true) {
                startActivity(Intent(this, HomeActivity::class.java))
                viewModel.clearStatus()
                finish()
            }
        }


        viewModel.errorMessage.observe(this) { msg ->
            msg?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

        // Go to Register
        binding.goToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Go to Forgot Password
        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    // ---------- REMEMBER ME ----------
    private fun saveRememberMe(email: String, password: String) {
        val prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("email", email)
            putString("password", password)
            apply()
        }
    }

    private fun loadRememberMe() {
        val prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        binding.emailInput.setText(prefs.getString("email", ""))
        binding.passwordInput.setText(prefs.getString("password", ""))
    }
}
