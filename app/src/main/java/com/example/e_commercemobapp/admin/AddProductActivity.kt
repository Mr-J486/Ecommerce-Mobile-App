package com.example.e_commercemobapp.admin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commercemobapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddProductActivity : AppCompatActivity() {

    private lateinit var productNameInput: EditText
    private lateinit var productDescriptionInput: EditText
    private lateinit var productPriceInput: EditText
    private lateinit var productStockInput: EditText
    private lateinit var productImageUrlInput: EditText
    private lateinit var saveProductBtn: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_product)

        FirebaseAuth.getInstance().signInAnonymously()

        // UI elements
        productNameInput = findViewById(R.id.productNameInput)
        productDescriptionInput = findViewById(R.id.productDescriptionInput)
        productPriceInput = findViewById(R.id.productPriceInput)
        productStockInput = findViewById(R.id.productStockInput)
        productImageUrlInput = findViewById(R.id.productImageUrlInput)
        saveProductBtn = findViewById(R.id.saveProductBtn)

        db = FirebaseFirestore.getInstance()

        saveProductBtn.setOnClickListener { saveProduct() }
    }

    private fun saveProduct() {
        val name = productNameInput.text.toString().trim()
        val description = productDescriptionInput.text.toString().trim()
        val priceStr = productPriceInput.text.toString().trim()
        val stockStr = productStockInput.text.toString().trim()
        val imageUrl = productImageUrlInput.text.toString().trim()

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceStr.toDouble()
        val stock = stockStr.toInt()

        val product = HashMap<String, Any?>()
        product["name"] = name
        product["description"] = description
        product["price"] = price
        product["stock"] = stock

        if (imageUrl.isNotEmpty()) {
            product["imageUrl"] = imageUrl
        }

        db.collection("products")
            .add(product)
            .addOnSuccessListener {
                Toast.makeText(this, "Product added!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
