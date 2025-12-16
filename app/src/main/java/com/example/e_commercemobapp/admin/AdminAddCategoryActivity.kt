package com.example.e_commercemobapp.admin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commercemobapp.R
import com.google.firebase.firestore.FirebaseFirestore

class AdminAddCategoryActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        val nameInput = findViewById<EditText>(R.id.categoryNameInput)
        val descInput = findViewById<EditText>(R.id.categoryDescriptionInput)
        val saveBtn = findViewById<Button>(R.id.saveCategoryBtn)

        saveBtn.setOnClickListener {

            val data = mapOf(
                "name" to nameInput.text.toString(),
                "description" to descInput.text.toString()
            )

            db.collection("categories").add(data)
                .addOnSuccessListener {
                    Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Add failed", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
