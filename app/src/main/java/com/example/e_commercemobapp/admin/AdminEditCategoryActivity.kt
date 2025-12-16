package com.example.e_commercemobapp.admin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commercemobapp.R
import com.google.firebase.firestore.FirebaseFirestore

class AdminEditCategoryActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private var categoryId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)

        // MATCHES YOUR XML EXACTLY
        val nameInput = findViewById<EditText>(R.id.editCategoryName)
        val descInput = findViewById<EditText>(R.id.editCategoryDescription)
        val updateBtn = findViewById<Button>(R.id.updateCategoryBtn)
        val deleteBtn = findViewById<Button>(R.id.deleteCategoryBtn)

        // Received from intent
        categoryId = intent.getStringExtra("id") ?: ""

        // Fill initial data
        nameInput.setText(intent.getStringExtra("name") ?: "")
        descInput.setText(intent.getStringExtra("description") ?: "")

        // Update category
        updateBtn.setOnClickListener {
            val newName = nameInput.text.toString().trim()
            val newDesc = descInput.text.toString().trim()

            if (newName.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updateData = mapOf(
                "name" to newName,
                "description" to newDesc
            )

            db.collection("categories").document(categoryId)
                .update(updateData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Category updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show()
                }
        }

        // Delete category
        deleteBtn.setOnClickListener {
            db.collection("categories").document(categoryId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Category deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
