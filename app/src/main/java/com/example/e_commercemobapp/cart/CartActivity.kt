package com.example.e_commercemobapp.cart

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobapp.R
import com.example.e_commercemobapp.search.SearchActivity
import android.content.Intent

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerCart: RecyclerView
    private lateinit var cartTotal: TextView
    private lateinit var backBtn: ImageButton
    private lateinit var checkoutBtn: Button
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerCart = findViewById(R.id.recyclerCart)
        cartTotal = findViewById(R.id.totalAmount)
        backBtn = findViewById(R.id.backBtn)
        checkoutBtn = findViewById(R.id.checkoutBtn)

        recyclerCart.layoutManager = LinearLayoutManager(this)

        adapter = CartAdapter(CartManager.getItems(), this)
        recyclerCart.adapter = adapter

        updateTotal()

        backBtn.setOnClickListener { finish() }

        checkoutBtn.setOnClickListener {
            if (CartManager.getItems().isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CartManager.clear()
            Toast.makeText(this, "Order Submitted ✔", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, SearchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    fun updateTotal() {
        cartTotal.text = "Total: ${CartManager.total()} EGP"
    }
}
