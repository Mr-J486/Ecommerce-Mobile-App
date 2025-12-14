package com.example.e_commercemobapp.cart

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobapp.R

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerCart: RecyclerView
    private lateinit var cartTotal: TextView
    private lateinit var backBtn: Button
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

        backBtn.setOnClickListener {
            finish()
        }

        checkoutBtn.setOnClickListener {
            cartTotal.text = "Order Submitted ✔"
        }
    }

    fun updateTotal() {
        cartTotal.text = "Total: ${CartManager.total()} EGP"
    }
}
