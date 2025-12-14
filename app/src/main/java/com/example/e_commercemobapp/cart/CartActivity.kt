package com.example.e_commercemobapp.cart

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobapp.R

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var totalText: TextView
    private lateinit var checkoutBtn: Button
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.recyclerCart)
        totalText = findViewById(R.id.cartTotal)
        checkoutBtn = findViewById(R.id.checkoutBtn)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CartAdapter(CartManager.getItems(), this)
        recyclerView.adapter = adapter

        updateTotal()

        checkoutBtn.setOnClickListener {
            Toast.makeText(this, "Order submitted!", Toast.LENGTH_SHORT).show()
            CartManager.clear()
            adapter.notifyDataSetChanged()
            updateTotal()
        }
    }

    fun updateTotal() {
        totalText.text = "Total: ${CartManager.total()} EGP"
    }
}
