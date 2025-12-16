package com.example.e_commercemobapp.admin

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commercemobapp.R
import com.example.e_commercemobapp.model.Category
import com.google.firebase.firestore.FirebaseFirestore

class AdminAddProductActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var descInput: EditText
    private lateinit var priceInput: EditText
    private lateinit var stockInput: EditText
    private lateinit var imageUrlInput: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var saveBtn: Button

    private val db = FirebaseFirestore.getInstance()
    private val categories = ArrayList<Category>()
    private var selectedCategoryId = ""

    private var editing = false
    private var productId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // Bind views from XML
        nameInput = findViewById(R.id.productNameInput)
        descInput = findViewById(R.id.productDescriptionInput)
        priceInput = findViewById(R.id.productPriceInput)
        stockInput = findViewById(R.id.productStockInput)
        imageUrlInput = findViewById(R.id.productImageUrlInput)
        categorySpinner = findViewById(R.id.categorySpinner)
        saveBtn = findViewById(R.id.saveProductBtn)

        loadCategories()
        checkIfEditing()

        saveBtn.setOnClickListener {
            if (editing) updateProduct() else saveProduct()
        }
    }

    private fun loadCategories() {
        db.collection("categories").get()
            .addOnSuccessListener { snap ->
                categories.clear()
                val names = ArrayList<String>()

                for (doc in snap) {
                    val c = Category(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        description = doc.getString("description") ?: ""
                    )
                    categories.add(c)
                    names.add(c.name)
                }

                // Populate spinner
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    names
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categorySpinner.adapter = adapter

                // Corrected listener (using `v` instead of `view`)
                categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        v: View?,
                        pos: Int,
                        id: Long
                    ) {
                        selectedCategoryId = categories[pos].id
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

                // Set correct category if editing
                if (editing) {
                    val editCategoryId = intent.getStringExtra("categoryId")
                    val index = categories.indexOfFirst { it.id == editCategoryId }
                    if (index >= 0) categorySpinner.setSelection(index)
                }
            }
    }

    private fun checkIfEditing() {
        editing = intent.getBooleanExtra("edit", false)

        if (editing) {
            productId = intent.getStringExtra("id")!!

            nameInput.setText(intent.getStringExtra("name"))
            descInput.setText(intent.getStringExtra("description"))
            priceInput.setText(intent.getDoubleExtra("price", 0.0).toString())
            stockInput.setText(intent.getIntExtra("stock", 0).toString())
            imageUrlInput.setText(intent.getStringExtra("imageUrl"))
        }
    }

    private fun saveProduct() {
        val data = mapOf(
            "name" to nameInput.text.toString(),
            "description" to descInput.text.toString(),
            "price" to priceInput.text.toString().toDoubleOrNull(),
            "stock" to stockInput.text.toString().toIntOrNull(),
            "categoryId" to selectedCategoryId,
            "barcode" to "", // optional
            "imageUrl" to imageUrlInput.text.toString()
        )

        db.collection("products").add(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun updateProduct() {
        val data = mapOf(
            "name" to nameInput.text.toString(),
            "description" to descInput.text.toString(),
            "price" to priceInput.text.toString().toDoubleOrNull(),
            "stock" to stockInput.text.toString().toIntOrNull(),
            "categoryId" to selectedCategoryId,
            "imageUrl" to imageUrlInput.text.toString()
        )

        db.collection("products").document(productId).update(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Product Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}
