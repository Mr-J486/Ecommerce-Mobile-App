package com.example.e_commercemobapp.admin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.example.e_commercemobapp.R

class EditCategoryActivity : AppCompatActivity() {
    private var editCategoryName: EditText? = null
    private var editCategoryDescription: EditText? = null
    private var updateCategoryBtn: Button? = null
    private var deleteCategoryBtn: Button? = null

    private var db: FirebaseFirestore? = null
    private var categoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)

        editCategoryName = findViewById<EditText?>(R.id.editCategoryName)
        editCategoryDescription = findViewById<EditText?>(R.id.editCategoryDescription)
        updateCategoryBtn = findViewById<Button?>(R.id.updateCategoryBtn)
        deleteCategoryBtn = findViewById<Button?>(R.id.deleteCategoryBtn)

        db = FirebaseFirestore.getInstance()

        // Receive data from adapter
        categoryId = getIntent().getStringExtra("id")
        val name = getIntent().getStringExtra("name")
        val description = getIntent().getStringExtra("description")

        editCategoryName!!.setText(name)
        editCategoryDescription!!.setText(description)

        updateCategoryBtn!!.setOnClickListener(View.OnClickListener { v: View? -> updateCategory() })
        deleteCategoryBtn!!.setOnClickListener(View.OnClickListener { v: View? -> deleteCategory() })
    }

    private fun updateCategory() {
        val newName = editCategoryName!!.getText().toString().trim { it <= ' ' }
        val newDesc = editCategoryDescription!!.getText().toString().trim { it <= ' ' }

        db!!.collection("categories")
            .document(categoryId!!)
            .update("name", newName, "description", newDesc)
            .addOnSuccessListener(OnSuccessListener { aVoid: Void? ->
                Toast.makeText(this, "Category updated!", Toast.LENGTH_SHORT).show()
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

    private fun deleteCategory() {
        db!!.collection("categories")
            .document(categoryId!!)
            .delete()
            .addOnSuccessListener(OnSuccessListener { aVoid: Void? ->
                Toast.makeText(this, "Category deleted!", Toast.LENGTH_SHORT).show()
                finish()
            })
            .addOnFailureListener(OnFailureListener { e: Exception? ->
                Toast.makeText(
                    this,
                    "Delete error: " + e!!.message,
                    Toast.LENGTH_LONG
                ).show()
            }
            )
    }
}