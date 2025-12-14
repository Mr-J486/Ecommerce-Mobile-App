//package com.example.e_commercemobapp.search
//
//import android.content.Intent
//import android.os.Bundle
//import android.speech.RecognizerIntent
//import android.text.Editable
//import android.text.TextWatcher
//import android.widget.*
//import androidx.activity.OnBackPressedCallback
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.e_commercemobapp.R
//import com.example.e_commercemobapp.cart.CartActivity
//import com.example.e_commercemobapp.cart.CartManager
//import com.example.e_commercemobapp.model.Product
//import com.example.e_commercemobapp.ui.auth.LoginActivity
//import java.util.Locale
//
//class SearchActivity : AppCompatActivity() {
//
//    // UI
//    private lateinit var searchInput: EditText
//    private lateinit var voiceBtn: ImageButton
//    private lateinit var barcodeBtn: ImageButton
//    private lateinit var openCartBtn: Button
//    private lateinit var logoutBtn: Button
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var cartTotalText: TextView
//
//    // Data
//    private lateinit var adapter: ProductAdapter
//    private val productList = mutableListOf<Product>()
//    private val filteredList = mutableListOf<Product>()
//
//    // 🔑 Track search click
//    private var hasSearchedOnce = false
//
//    // =========================
//    // BARCODE RESULT
//    // =========================
//    private val barcodeLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK) {
//                val scanned = result.data?.getStringExtra("barcode") ?: return@registerForActivityResult
//
//                searchInput.setText(scanned)
//
//                filteredList.clear()
//                productList.forEach {
//                    if (it.barcode == scanned) filteredList.add(it)
//                }
//
//                adapter.notifyDataSetChanged()
//            }
//        }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_search)
//
//        // UI
//        searchInput = findViewById(R.id.searchInput)
//        searchInput.setOnClickListener {
//            searchInput.setText("")
//            filteredList.clear()
//            filteredList.addAll(productList)
//            adapter.notifyDataSetChanged()
//        }
//
//        voiceBtn = findViewById(R.id.voiceBtn)
//        barcodeBtn = findViewById(R.id.barcodeBtn)
//        openCartBtn = findViewById(R.id.openCartBtn)
//        logoutBtn = findViewById(R.id.logoutBtn)
//        recyclerView = findViewById(R.id.recyclerSearch)
//        cartTotalText = findViewById(R.id.cartTotalText)
//
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        // Products
//        productList.addAll(
//            listOf(
//                Product("1", "Milk", 20.0, "11111"),
//                Product("2", "Sugar", 15.5, "22222"),
//                Product("3", "Coffee", 40.0, "33333"),
//                Product("4", "Rice", 50.0, "44444")
//            )
//        )
//
//        filteredList.addAll(productList)
//        adapter = ProductAdapter(filteredList, this)
//        recyclerView.adapter = adapter
//
//        updateCartTotal()
//
//        setupTextSearch()
//        setupVoiceSearch()
//        setupBarcodeSearch()
//
//        // CART
//        openCartBtn.setOnClickListener {
//            startActivity(Intent(this, CartActivity::class.java))
//        }
//
//        // LOGOUT
//        logoutBtn.setOnClickListener {
//            CartManager.clear()
//
//            val intent = Intent(this, LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//            finish()
//        }
//
//        // 🔙 BACK BUTTON — MINIMIZE APP
//        onBackPressedDispatcher.addCallback(
//            this,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    moveTaskToBack(true)
//                }
//            }
//        )
//    }
//
//    // =========================
//    // CART TOTAL
//    // =========================
//    fun updateCartTotal() {
//        cartTotalText.text = "Total: ${CartManager.total()} EGP"
//    }
//
//    // =========================
//    // TEXT SEARCH + CLEAR ON RECLICK
//    // =========================
//    private fun setupTextSearch() {
//
//        searchInput.setOnClickListener {
//            if (hasSearchedOnce) {
//                searchInput.setText("")
//                filteredList.clear()
//                filteredList.addAll(productList)
//                adapter.notifyDataSetChanged()
//                hasSearchedOnce = false
//            }
//        }
//
//        searchInput.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {}
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
//                val q = query.toString().trim().lowercase()
//                filteredList.clear()
//
//                if (q.isEmpty()) {
//                    filteredList.addAll(productList)
//                } else {
//                    filteredList.addAll(
//                        productList.filter { product ->
//                            product.name.lowercase().contains(q) ||
//                                    product.barcode.trim().contains(q)
//                        }
//                    )
//                }
//
//                adapter.notifyDataSetChanged()
//                hasSearchedOnce = q.isNotEmpty()
//            }
//        })
//    }
//
//
//    // =========================
//    // VOICE SEARCH
//    // =========================
//    private fun setupVoiceSearch() {
//        voiceBtn.setOnClickListener {
//            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//            intent.putExtra(
//                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
//            )
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
//
//            try {
//                startActivityForResult(intent, 200)
//            } catch (e: Exception) {
//                Toast.makeText(this, "Voice search not supported", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    // =========================
//    // BARCODE SEARCH
//    // =========================
//    private fun setupBarcodeSearch() {
//        barcodeBtn.setOnClickListener {
//            val intent = Intent(this, BarcodeScannerActivity::class.java)
//            barcodeLauncher.launch(intent)
//        }
//    }
//
//    // =========================
//    // VOICE RESULT
//    // =========================
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == 200 && resultCode == RESULT_OK) {
//            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
//            searchInput.setText(result?.getOrNull(0) ?: "")
//        }
//    }
//}
package com.example.e_commercemobapp.search

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobapp.R
import com.example.e_commercemobapp.cart.CartActivity
import com.example.e_commercemobapp.cart.CartManager
import com.example.e_commercemobapp.model.Product
import com.example.e_commercemobapp.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    // UI
    private lateinit var searchInput: EditText
    private lateinit var voiceBtn: ImageButton
    private lateinit var barcodeBtn: ImageButton
    private lateinit var openCartBtn: Button
    private lateinit var logoutBtn: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartTotalText: TextView

    // Data
    private lateinit var adapter: ProductAdapter
    private val productList = mutableListOf<Product>()
    private val filteredList = mutableListOf<Product>()

    // Track search usage
    private var hasSearchedOnce = false

    // =========================
    // BARCODE RESULT
    // =========================
    private val barcodeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val scanned = result.data?.getStringExtra("barcode") ?: return@registerForActivityResult

                searchInput.setText(scanned)

                filteredList.clear()
                productList.forEach {
                    if (it.barcode == scanned) filteredList.add(it)
                }

                adapter.notifyDataSetChanged()
                hasSearchedOnce = true
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // UI binding
        searchInput = findViewById(R.id.searchInput)
        voiceBtn = findViewById(R.id.voiceBtn)
        barcodeBtn = findViewById(R.id.barcodeBtn)
        openCartBtn = findViewById(R.id.openCartBtn)
        logoutBtn = findViewById(R.id.logoutBtn)
        recyclerView = findViewById(R.id.recyclerSearch)
        cartTotalText = findViewById(R.id.cartTotalText)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // PRODUCTS
        productList.addAll(
            listOf(
                Product("1", "Milk", 20.0, "11111"),
                Product("2", "Sugar", 15.5, "22222"),
                Product("3", "Coffee", 40.0, "33333"),
                Product("4", "Rice", 50.0, "44444")
            )
        )

        filteredList.addAll(productList)
        adapter = ProductAdapter(filteredList, this)
        recyclerView.adapter = adapter

        updateCartTotal()

        setupTextSearch()
        setupVoiceSearch()
        setupBarcodeSearch()

        // CART
        openCartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        // 🔐 LOGOUT (SECURE)
        logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            CartManager.clear()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // 🔙 BACK BUTTON → minimize app
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    moveTaskToBack(true)
                }
            }
        )
    }

    // =========================
    // CART TOTAL
    // =========================
    fun updateCartTotal() {
        cartTotalText.text = "Total: ${CartManager.total()} EGP"
    }

    // =========================
    // TEXT SEARCH + CLEAR ON RECLICK
    // =========================
    private fun setupTextSearch() {

        searchInput.setOnClickListener {
            if (hasSearchedOnce) {
                searchInput.setText("")
                filteredList.clear()
                filteredList.addAll(productList)
                adapter.notifyDataSetChanged()
                hasSearchedOnce = false
            }
        }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
                val q = query.toString().trim().lowercase()
                filteredList.clear()

                if (q.isEmpty()) {
                    filteredList.addAll(productList)
                } else {
                    filteredList.addAll(
                        productList.filter {
                            it.name.lowercase().contains(q) ||
                                    it.barcode.contains(q)
                        }
                    )
                }

                adapter.notifyDataSetChanged()
                hasSearchedOnce = q.isNotEmpty()
            }
        })
    }

    // =========================
    // VOICE SEARCH
    // =========================
    private fun setupVoiceSearch() {
        voiceBtn.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

            try {
                startActivityForResult(intent, 200)
            } catch (e: Exception) {
                Toast.makeText(this, "Voice search not supported", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // =========================
    // BARCODE SEARCH
    // =========================
    private fun setupBarcodeSearch() {
        barcodeBtn.setOnClickListener {
            searchInput.setText("")
            filteredList.clear()
            filteredList.addAll(productList)
            adapter.notifyDataSetChanged()

            val intent = Intent(this, BarcodeScannerActivity::class.java)
            barcodeLauncher.launch(intent)
        }
    }

    // =========================
    // VOICE RESULT
    // =========================
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200 && resultCode == RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            searchInput.setText(result?.getOrNull(0) ?: "")
        }
    }
}
