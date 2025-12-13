package com.example.e_commercemobapp.ui.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commercemobapp.databinding.ActivityRegisterBinding
import com.example.e_commercemobapp.model.User
import com.example.e_commercemobapp.ui.home.HomeActivity
import com.example.e_commercemobapp.viewmodel.AuthViewModel
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.birthdayInput.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this,
                { _, y, m, d ->
                    binding.birthdayInput.setText("%02d/%02d/%04d".format(d, m + 1, y))
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.maxDate = System.currentTimeMillis()
            }.show()
        }

        binding.registerBtn.setOnClickListener {
            val user = User(
                binding.nameInput.text.toString(),
                binding.emailInput.text.toString(),
                binding.birthdayInput.text.toString()
            )
            viewModel.register(
                binding.emailInput.text.toString(),
                binding.passwordInput.text.toString(),
                user
            )
        }

        viewModel.authStatus.observe(this) {
            if (it == true) {
                startActivity(Intent(this, HomeActivity::class.java))
                viewModel.clearStatus()
                finish()
            }
        }
    }
}
