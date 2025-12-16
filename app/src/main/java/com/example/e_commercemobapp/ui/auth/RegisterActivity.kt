package com.example.e_commercemobapp.ui.auth

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.e_commercemobapp.databinding.ActivityRegisterBinding
import com.example.e_commercemobapp.model.User
import com.example.e_commercemobapp.viewmodel.AuthViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    private val fusedLocation by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // =========================
    // LOCATION PERMISSION
    // =========================
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) fetchLocation()
            else Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // =========================
        // BIRTHDAY PICKER
        // =========================
        binding.birthdayInput.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    binding.birthdayInput.setText(
                        "%02d/%02d/%04d".format(d, m + 1, y)
                    )
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.maxDate = System.currentTimeMillis()
            }.show()
        }

        // =========================
        // LOCATION BUTTON
        // =========================
        binding.getLocationBtn.setOnClickListener {
            requestLocationPermission()
        }

        // =========================
        // REGISTER BUTTON
        // =========================
        binding.registerBtn.setOnClickListener {

            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString()
            val name = binding.nameInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.registerBtn.isEnabled = false
            binding.registerBtn.text = "Creating..."

            val user = User(
                name = name,
                email = email,
                birthday = binding.birthdayInput.text.toString(),
                location = binding.locationInput.text.toString(),
                admin  = false
            )

            viewModel.register(email, password, user)
        }

        // =========================
        // OBSERVE REGISTER RESULT
        // =========================
        viewModel.authStatus.observe(this) { event ->
            event?.getContentIfNotHandled()?.let { success ->
                if (success) {
                    Toast.makeText(
                        this,
                        "Account created successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    resetRegisterButton()
                }
            }
        }

        // =========================
        // OBSERVE ERRORS
        // =========================
        viewModel.errorMessage.observe(this) { event ->
            event?.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                resetRegisterButton()
            }
        }
    }

    // =========================
    // LOCATION LOGIC
    // =========================
    private fun requestLocationPermission() {
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchLocation()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @Suppress("MissingPermission")
    private fun fetchLocation() {
        binding.locationInput.setText("Getting location...")

        fusedLocation.lastLocation.addOnSuccessListener { loc ->
            if (loc == null) {
                binding.locationInput.setText("")
                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }

            scope.launch {
                val address = withContext(Dispatchers.IO) {
                    try {
                        val geocoder = Geocoder(this@RegisterActivity, Locale.getDefault())
                        geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                            ?.firstOrNull()
                            ?.getAddressLine(0)
                    } catch (e: Exception) {
                        null
                    }
                }

                binding.locationInput.setText(
                    address ?: "${loc.latitude}, ${loc.longitude}"
                )
            }
        }
    }

    // =========================
    // RESET BUTTON
    // =========================
    private fun resetRegisterButton() {
        binding.registerBtn.isEnabled = true
        binding.registerBtn.text = "Create Account"
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
