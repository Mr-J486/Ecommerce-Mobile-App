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
//import com.google.firebase.auth.FirebaseAuth
//import java.util.Locale
//
//class SearchActivity : AppCompatActivity() {
//
//    private lateinit var searchInput: EditText
//    private lateinit var voiceBtn: ImageButton
//    private lateinit var barcodeBtn: ImageButton
//    private lateinit var openCartBtn: Button
//    private lateinit var logoutBtn: Button
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var cartTotalText: TextView
//
//    private lateinit var adapter: ProductAdapter
//    private val productList = mutableListOf<Product>()
//    private val filteredList = mutableListOf<Product>()
//
//    // BARCODE RESULT
//    private val barcodeLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK) {
//                val scanned = result.data?.getStringExtra("barcode") ?: return@registerForActivityResult
//                searchInput.setText(scanned)
//                filterProducts(scanned)
//            }
//        }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_search)
//
//        searchInput = findViewById(R.id.searchInput)
//        voiceBtn = findViewById(R.id.voiceBtn)
//        barcodeBtn = findViewById(R.id.barcodeBtn)
//        openCartBtn = findViewById(R.id.openCartBtn)
//        logoutBtn = findViewById(R.id.logoutBtn)
//        recyclerView = findViewById(R.id.recyclerSearch)
//        cartTotalText = findViewById(R.id.cartTotalText)
//
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        // PRODUCTS
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
//        // OPEN CART
//        openCartBtn.setOnClickListener {
//            startActivity(Intent(this, CartActivity::class.java))
//        }
//
//        // LOGOUT
//        logoutBtn.setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
//            CartManager.clear()
//
//            val intent = Intent(this, LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//            finish()
//        }
//
//        // BACK BUTTON closes app (not go back to login)
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
//    // UPDATE CART TOTAL
//    fun updateCartTotal() {
//        cartTotalText.text = "Total: ${CartManager.total()} EGP"
//    }
//
//    // TEXT SEARCH
//    private fun setupTextSearch() {
//
//        // Click clears instantly
//        searchInput.setOnClickListener {
//            searchInput.setText("")
//            filterProducts("")
//        }
//
//        // Typing filters live
//        searchInput.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {}
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
//                filterProducts(query.toString())
//            }
//        })
//    }
//
//    // VOICE SEARCH
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
//    // BARCODE SEARCH
//    private fun setupBarcodeSearch() {
//        barcodeBtn.setOnClickListener {
//            searchInput.setText("")
//            filterProducts("")
//
//            val intent = Intent(this, BarcodeScannerActivity::class.java)
//            barcodeLauncher.launch(intent)
//        }
//    }
//
//    // VOICE RESULT HANDLER
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == 200 && resultCode == RESULT_OK) {
//            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
//            val text = result?.getOrNull(0) ?: ""
//
//            searchInput.setText(text)
//            filterProducts(text)
//        }
//    }
//
//    // MAIN FILTER FUNCTION
//    private fun filterProducts(query: String) {
//        val q = query.trim().lowercase()
//        filteredList.clear()
//
//        if (q.isEmpty()) {
//            filteredList.addAll(productList)
//        } else {
//            filteredList.addAll(
//                productList.filter {
//                    it.name.lowercase().contains(q) || it.barcode.contains(q)
//                }
//            )
//        }
//
//        adapter.notifyDataSetChanged()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        updateCartTotal()
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
import com.example.e_commercemobapp.model.Category
import com.example.e_commercemobapp.model.Product
import com.example.e_commercemobapp.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    private lateinit var searchInput: EditText
    private lateinit var voiceBtn: ImageButton
    private lateinit var barcodeBtn: ImageButton
    private lateinit var openCartBtn: Button
    private lateinit var logoutBtn: Button

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartTotalText: TextView
    private lateinit var categoryRecycler: RecyclerView

    private lateinit var adapter: ProductAdapter
    private val productList = mutableListOf<Product>()
    private val filteredList = mutableListOf<Product>()

    // ---------------------------------------------------
    // BARCODE RESULT HANDLER
    // ---------------------------------------------------
    private val barcodeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val scanned = result.data?.getStringExtra("barcode") ?: return@registerForActivityResult
                searchInput.setText(scanned)
                filterProducts(scanned)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        bindViews()
        setupRecyclerViews()
        loadProducts()
        loadCategories()

        updateCartTotal()

        setupTextSearch()
        setupVoiceSearch()
        setupBarcodeSearch()
        setupButtons()

        // BACK → minimize app
        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    moveTaskToBack(true)
                }
            }
        )
    }

    private fun bindViews() {
        searchInput = findViewById(R.id.searchInput)
        voiceBtn = findViewById(R.id.voiceBtn)
        barcodeBtn = findViewById(R.id.barcodeBtn)
        openCartBtn = findViewById(R.id.openCartBtn)
        logoutBtn = findViewById(R.id.logoutBtn)

        recyclerView = findViewById(R.id.recyclerSearch)
        cartTotalText = findViewById(R.id.cartTotalText)
        categoryRecycler = findViewById(R.id.categoryRecycler)
    }

    private fun setupRecyclerViews() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        categoryRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    // ---------------------------------------------------
    // LOAD PRODUCTS (ADD YOUR FULL DATABASE HERE)
    // ---------------------------------------------------
    private fun loadProducts() {
        productList.addAll(
            listOf(
                Product("1", "Milk", 20.0, "11111", "Food"),
                Product("2", "Sugar", 15.5, "22222", "Food"),
                Product("3", "Coffee", 40.0, "33333", "Food"),
                Product("4", "Rice", 50.0, "44444", "Food"),

                Product("5", "Smartphone X", 12500.0, "55555", "Electronics"),
                Product("6", "Laptop Pro", 25000.0, "66666", "Electronics"),
                Product("7", "USB-C Cable", 150.0, "77777", "Accessories"),
                Product("8", "Headphones", 850.0, "88888", "Accessories"),
                Product("9", "Men T-shirt", 250.0, "99999", "Clothes"),
                Product("10", "Women Jacket", 450.0, "10101", "Clothes")
            )
        )

        filteredList.addAll(productList)
        adapter = ProductAdapter(filteredList, this)
        recyclerView.adapter = adapter
    }

    // ---------------------------------------------------
    // LOAD CATEGORIES
    // ---------------------------------------------------
    private fun loadCategories() {
        val categories = listOf(
            Category("All", R.drawable.ic_cat_all),
            Category("Electronics", R.drawable.ic_cat_electronics),
            Category("Accessories", R.drawable.ic_cat_accessories),
            Category("Clothes", R.drawable.ic_cat_clothes),
            Category("Food", R.drawable.ic_cat_food)
        )

        categoryRecycler.adapter = CategoryAdapter(categories) { selectedCategory ->
            if (selectedCategory == "All") {
                filterProducts("")
                searchInput.setText("")
            } else {
                searchInput.setText(selectedCategory)
                filterProducts(selectedCategory)
            }
        }
    }

    // ---------------------------------------------------
    // CART + LOGOUT BUTTONS
    // ---------------------------------------------------
    private fun setupButtons() {

        openCartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            CartManager.clear()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    // ---------------------------------------------------
    // UPDATE CART TOTAL
    // ---------------------------------------------------
    fun updateCartTotal() {
        cartTotalText.text = "Total: ${CartManager.total()} EGP"
    }

    // ---------------------------------------------------
    // SEARCH HANDLERS
    // ---------------------------------------------------
    private fun setupTextSearch() {

        // Clicking inside clears instantly
        searchInput.setOnClickListener {
            searchInput.setText("")
            filterProducts("")
        }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
                filterProducts(query.toString())
            }
        })
    }

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

    private fun setupBarcodeSearch() {
        barcodeBtn.setOnClickListener {
            searchInput.setText("")
            filterProducts("")
            barcodeLauncher.launch(Intent(this, BarcodeScannerActivity::class.java))
        }
    }

    // ---------------------------------------------------
    // VOICE SEARCH RESULT
    // ---------------------------------------------------
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200 && resultCode == RESULT_OK) {
            val spoken = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0) ?: ""
            searchInput.setText(spoken)
            filterProducts(spoken)
        }
    }

    // ---------------------------------------------------
    // FILTER PRODUCTS BY NAME, CATEGORY OR BARCODE
    // ---------------------------------------------------
    private fun filterProducts(query: String) {
        val q = query.trim().lowercase()
        filteredList.clear()

        if (q.isEmpty()) {
            filteredList.addAll(productList)
        } else {
            filteredList.addAll(
                productList.filter {
                    it.name.lowercase().contains(q) ||
                            it.category.lowercase().contains(q) ||
                            it.barcode.contains(q)
                }
            )
        }

        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        updateCartTotal()
    }
}

