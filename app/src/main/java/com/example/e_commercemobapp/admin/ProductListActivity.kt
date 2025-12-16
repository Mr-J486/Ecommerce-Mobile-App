package com.example.e_commercemobapp.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.example.e_commercemobapp.R

class ProductListActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var adapter: ProductAdapter? = null
    private val productList = ArrayList<Product?>()
    private var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        recyclerView = findViewById<RecyclerView?>(R.id.productRecyclerView)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))

        adapter = ProductAdapter(this, productList)
        recyclerView!!.setAdapter(adapter)

        val addProductBtn = findViewById<Button?>(R.id.addProductBtn)
        addProductBtn.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@ProductListActivity,
                    AddProductActivity::class.java
                )
            )
        }
        )

        db = FirebaseFirestore.getInstance()
        loadProducts()
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }

    private fun loadProducts() {
        db!!.collection("products")
            .get()
            .addOnSuccessListener(OnSuccessListener { query: QuerySnapshot? ->
                productList.clear()
                for (doc in query!!) {
                    val id = doc.getId()
                    val name = doc.getString("name")
                    val description = doc.getString("description")
                    val price = doc.getDouble("price")

                    val qtyLong = doc.getLong("stock")
                    val qty = if (qtyLong != null) qtyLong.toInt() else 0

                    val categoryName = doc.getString("categoryName")
                    val imageUrl = doc.getString("imageUrl")

                    productList.add(
                        Product(
                            id, name, description,
                            if (price != null) price else 0.0,
                            qty,
                            categoryName,
                            imageUrl
                        )
                    )
                }
                adapter!!.notifyDataSetChanged()
            })
            .addOnFailureListener(OnFailureListener { e: Exception? ->
                Log.e(
                    "Firestore",
                    "Error loading products",
                    e
                )
            }
            )
    }
}