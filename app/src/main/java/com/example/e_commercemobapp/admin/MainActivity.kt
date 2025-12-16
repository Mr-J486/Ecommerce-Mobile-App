package com.example.e_commercemobapp.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commercemobapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnCategories = findViewById<Button?>(R.id.btnCategories)
        val btnProducts = findViewById<Button?>(R.id.btnProducts)
        val btnReports = findViewById<Button?>(R.id.btnReports)

        btnCategories.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@MainActivity,
                    CategoryListActivity::class.java
                )
            )
        }
        )

        btnProducts.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@MainActivity,
                    ProductListActivity::class.java
                )
            )
        }
        )

        btnReports.setOnClickListener(View.OnClickListener { v: View? -> })
    }
}