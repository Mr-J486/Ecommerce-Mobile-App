package com.example.e_commercemobapp.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commercemobapp.databinding.ActivityForgotPasswordBinding
import com.example.e_commercemobapp.viewmodel.AuthViewModel

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resetPasswordBtn.setOnClickListener {
            val email = binding.emailInput.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.resetPassword(email)
        }

        viewModel.authStatus.observe(this) { success ->
            if (success == true) {
                Toast.makeText(this, "Reset email sent!", Toast.LENGTH_SHORT).show()
                viewModel.clearStatus()
                finish()
            }
        }

        viewModel.errorMessage.observe(this) { msg ->
            msg?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
    }
}
