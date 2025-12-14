package com.example.e_commercemobapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commercemobapp.ui.auth.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Go directly to Login (NO Home)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
