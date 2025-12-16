package com.example.e_commercemobapp.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobapp.R
import com.example.e_commercemobapp.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import android.view.View
import android.widget.Button

class AdminProductListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminProductAdapter
    private val products = ArrayList<Product>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        recyclerView = findViewById(R.id.productRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // FIXED ADAPTER (now accepts click lambda)
        adapter = AdminProductAdapter(this, products) { selectedProduct ->

            val intent = Intent(this, AdminAddProductActivity::class.java)
            intent.putExtra("edit", true)
            intent.putExtra("id", selectedProduct.id)
            intent.putExtra("name", selectedProduct.name)
            intent.putExtra("description", selectedProduct.description)
            intent.putExtra("imageUrl", selectedProduct.imageUrl)
            intent.putExtra("price", selectedProduct.price)
            intent.putExtra("stock", selectedProduct.stock)
            intent.putExtra("categoryId", selectedProduct.categoryId)
            intent.putExtra("barcode", selectedProduct.barcode)

            startActivity(intent)
        }

        recyclerView.adapter = adapter

        findViewById<View>(R.id.addProductBtn).setOnClickListener {
            startActivity(Intent(this, AdminAddProductActivity::class.java))
        }

        loadProducts()
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }

    private fun loadProducts() {
        db.collection("products")
            .get()
            .addOnSuccessListener { snap ->
                products.clear()
                for (doc in snap) {
                    products.add(
                        Product(
                            id = doc.id,
                            name = doc.getString("name") ?: "",
                            description = doc.getString("description") ?: "",
                            imageUrl = doc.getString("imageUrl") ?: "",
                            price = doc.getDouble("price") ?: 0.0,
                            stock = (doc.getLong("stock") ?: 0).toInt(),
                            categoryId = doc.getString("categoryId") ?: "",
                            barcode = doc.getString("barcode") ?: ""
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }
    }
}
