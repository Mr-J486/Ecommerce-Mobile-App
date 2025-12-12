//package com.example.e_commercemobapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import android.widget.*
//
//class CartActivity : AppCompatActivity() {
//
//    private lateinit var recyclerCart: RecyclerView
//    private lateinit var totalText: TextView
//    private lateinit var checkoutBtn: Button
//    private lateinit var backBtn: Button
//    private lateinit var orderStatus: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_cart)
//
//        recyclerCart = findViewById(R.id.recyclerCart)
//        totalText = findViewById(R.id.totalPriceText)
//        checkoutBtn = findViewById(R.id.checkoutBtn)
//        backBtn = findViewById(R.id.backToSearchBtn)
//        orderStatus = findViewById(R.id.orderStatusText)
//
//        recyclerCart.layoutManager = LinearLayoutManager(this)
//        recyclerCart.adapter = CartAdapter(CartManager.cartItems, this)
//
//        updateTotal()
//
//        backBtn.setOnClickListener {
//            val intent = Intent(this, SearchActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//            finish()
//        }
//
//
//
//        checkoutBtn.setOnClickListener {
//            if (CartManager.cartItems.isEmpty()) {
//                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            orderStatus.text = "Order Submitted ✔"
//        }
//    }
//
//    fun updateTotal() {
//        totalText.text = "Total: ${CartManager.total()} EGP"
//    }
//}
package com.example.e_commercemobapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
