package com.example.e_commercemobapp.cart

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobapp.search.CartAdapter
import com.example.e_commercemobapp.search.CartManager
import com.example.e_commercemobapp.R

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerCart: RecyclerView
    private lateinit var totalPriceText: TextView
    private lateinit var checkoutBtn: Button
    private lateinit var backBtn: Button
    private lateinit var orderStatus: TextView

    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerCart = findViewById(R.id.recyclerCart)
        totalPriceText = findViewById(R.id.totalPriceText)
        checkoutBtn = findViewById(R.id.checkoutBtn)
        backBtn = findViewById(R.id.backToSearchBtn)
        orderStatus = findViewById(R.id.orderStatusText)

        recyclerCart.layoutManager = LinearLayoutManager(this)

        adapter = CartAdapter(CartManager.cartItems, this)
        recyclerCart.adapter = adapter

        updateTotal()

        // BACK BUTTON
        backBtn.setOnClickListener {
            clearOrderStatus()
            finish()
        }

        // CHECKOUT BUTTON
        checkoutBtn.setOnClickListener {
            if (CartManager.cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            orderStatus.text = "Order Submitted ✔"
        }
    }

    fun updateTotal() {
        totalPriceText.text = "Total: ${CartManager.total()} EGP"
        clearOrderStatus()   // 🔥 Hide order submitted when cart changes
        adapter.notifyDataSetChanged()
    }

    fun clearOrderStatus() {
        orderStatus.text = ""
    }
}