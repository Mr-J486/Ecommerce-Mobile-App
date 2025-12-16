package com.example.e_commercemobapp.admin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.example.e_commercemobapp.R

class AddCategoryActivity : AppCompatActivity() {
    private var categoryNameInput: EditText? = null
    private var categoryDescriptionInput: EditText? = null
    private var saveCategoryBtn: Button? = null

    private var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        // UI elements
        categoryNameInput = findViewById<EditText?>(R.id.categoryNameInput)
        categoryDescriptionInput = findViewById<EditText?>(R.id.categoryDescriptionInput)
        saveCategoryBtn = findViewById<Button?>(R.id.saveCategoryBtn)

        // Firebase
        db = FirebaseFirestore.getInstance()

        saveCategoryBtn!!.setOnClickListener(View.OnClickListener { v: View? -> saveCategory() })
    }

    private fun saveCategory() {
        val name = categoryNameInput!!.getText().toString().trim { it <= ' ' }
        val description = categoryDescriptionInput!!.getText().toString().trim { it <= ' ' }

        if (name.isEmpty()) {
            Toast.makeText(this, "Category name is required", Toast.LENGTH_SHORT).show()
            return
        }

        val category: MutableMap<String?, Any?> = HashMap<String?, Any?>()
        category.put("name", name)
        category.put("description", description)

        db!!.collection("categories")
            .add(category)
            .addOnSuccessListener(OnSuccessListener { documentReference: DocumentReference? ->
                Toast.makeText(this, "Category added!", Toast.LENGTH_SHORT).show()
                finish()
            })
            .addOnFailureListener(OnFailureListener { e: Exception? ->
                Toast.makeText(
                    this,
                    "Error: " + e!!.message,
                    Toast.LENGTH_LONG
                ).show()
            }
            )
    }
}