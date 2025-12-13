//package com.example.e_commercemobapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.speech.RecognizerIntent
//import android.text.Editable
//import android.text.TextWatcher
//import android.widget.*
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import java.util.Locale
//
//class SearchActivity : AppCompatActivity() {
//
//    private lateinit var searchInput: EditText
//    private lateinit var voiceBtn: ImageButton
//    private lateinit var barcodeBtn: ImageButton
//    private lateinit var openCartBtn: Button
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var cartTotalText: TextView
//
//    private lateinit var adapter: ProductAdapter
//    private var productList = mutableListOf<Product>()
//    private var filteredList = mutableListOf<Product>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_search)
//
//        // Connect UI
//        searchInput = findViewById(R.id.searchInput)
//        voiceBtn = findViewById(R.id.voiceBtn)
//        barcodeBtn = findViewById(R.id.barcodeBtn)
//        openCartBtn = findViewById(R.id.openCartBtn)
//        recyclerView = findViewById(R.id.recyclerSearch)
//        cartTotalText = findViewById(R.id.cartTotalText)
//
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        // TEMP DATA
//        productList = mutableListOf(
//            Product("1", "Milk", 20.0, "11111"),
//            Product("2", "Sugar", 15.5, "22222"),
//            Product("3", "Coffee", 40.0, "33333"),
//            Product("4", "Rice", 50.0, "44444")
//        )
//
//        filteredList.addAll(productList)
//        adapter = ProductAdapter(filteredList, this)
//        recyclerView.adapter = adapter
//
//        updateCartTotal()   // show total initially
//
//        setupTextSearch()
//        setupVoiceSearch()
//        setupBarcodeSearch()
//
//        openCartBtn.setOnClickListener {
//            startActivity(Intent(this, CartActivity::class.java))
//        }
//    }
//
//    fun updateCartTotal() {
//        cartTotalText.text = "Total: ${CartManager.total()} EGP"
//    }
//
//    private fun setupTextSearch() {
//        searchInput.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {}
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
//                val q = query.toString().lowercase()
//                filteredList.clear()
//
//                for (p in productList) {
//                    if (p.name.lowercase().contains(q)) filteredList.add(p)
//                }
//
//                adapter.notifyDataSetChanged()
//            }
//        })
//    }
//
//    private fun setupVoiceSearch() {
//        voiceBtn.setOnClickListener {
//            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
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
//    private fun setupBarcodeSearch() {
//        barcodeBtn.setOnClickListener {
//            val fakeBarcode = "33333"
//            searchInput.setText(fakeBarcode)
//
//            filteredList.clear()
//            for (p in productList) {
//                if (p.barcode == fakeBarcode) filteredList.add(p)
//            }
//
//            adapter.notifyDataSetChanged()
//            Toast.makeText(this, "Simulated barcode scan: $fakeBarcode", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == 200 && resultCode == RESULT_OK) {
//            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
//            searchInput.setText(result?.get(0) ?: "")
//        }
//    }
//}
//
//// PRODUCT MODEL
//data class Product(
//    val id: String,
//    val name: String,
//    val price: Double,
//    val barcode: String,
//    var quantity: Int = 1
//)
//
//// CART STORAGE
//object CartManager {
//    val cartItems = mutableListOf<Product>()
//
//    fun add(product: Product) {
//        // If product already exists, increase quantity
//        val existing = cartItems.find { it.id == product.id }
//        if (existing != null) {
//            existing.quantity++
//        } else {
//            cartItems.add(product.copy(quantity = 1))
//        }
//    }
//
//    fun increase(p: Product) {
//        p.quantity++
//    }
//
//    fun decrease(p: Product) {
//        p.quantity--
//        if (p.quantity <= 0) {
//            cartItems.remove(p)
//        }
//    }
//
//    fun total(): Double {
//        return cartItems.sumOf { it.price * it.quantity }
//    }
//}
//
//
//// CLEAN ADAPTER
//class ProductAdapter(private val items: MutableList<Product>, private val activity: SearchActivity) :
//    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
//
//    inner class ProductViewHolder(val view: android.view.View) :
//        RecyclerView.ViewHolder(view) {
//        val name: TextView = view.findViewById(R.id.productName)
//        val price: TextView = view.findViewById(R.id.productPrice)
//        val addBtn: Button = view.findViewById(R.id.addToCartBtn)
//    }
//
//    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ProductViewHolder {
//        val v = android.view.LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_product, parent, false)
//        return ProductViewHolder(v)
//    }
//
//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        val p = items[position]
//
//        holder.name.text = p.name
//        holder.price.text = "${p.price} EGP"
//
//        holder.addBtn.setOnClickListener {
//            CartManager.add(p)
//            Toast.makeText(holder.view.context, "${p.name} added to cart", Toast.LENGTH_SHORT).show()
//
//            // Update total in SearchActivity
//            activity.updateCartTotal()
//        }
//    }
//
//    override fun getItemCount(): Int = items.size
//}
//
//class CartAdapter(private val items: MutableList<Product>, private val activity: CartActivity) :
//    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
//
//    inner class CartViewHolder(val view: android.view.View) :
//        RecyclerView.ViewHolder(view) {
//
//        val name: TextView = view.findViewById(R.id.productName)
//        val price: TextView = view.findViewById(R.id.productPrice)
//    }
//
//    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): CartViewHolder {
//        val v = android.view.LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_product, parent, false)
//        return CartViewHolder(v)
//    }
//
//    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
//        val p = items[position]
//
//        holder.name.text = p.name
//        holder.price.text = "${p.price} EGP"
//    }
//
//    override fun getItemCount(): Int = items.size
//}

package com.example.e_commercemobapp.search

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobapp.R
import com.example.e_commercemobapp.cart.CartActivity
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    private lateinit var searchInput: EditText
    private lateinit var voiceBtn: ImageButton
    private lateinit var barcodeBtn: ImageButton
    private lateinit var openCartBtn: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartTotalText: TextView

    private lateinit var adapter: ProductAdapter
    private var productList = mutableListOf<Product>()
    private var filteredList = mutableListOf<Product>()

    private val barcodeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val scanned = result.data?.getStringExtra("barcode") ?: return@registerForActivityResult

                searchInput.setText(scanned)

                filteredList.clear()
                for (p in productList) {
                    if (p.barcode == scanned) filteredList.add(p)
                }

                adapter.notifyDataSetChanged()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Connect UI
        searchInput = findViewById(R.id.searchInput)
        voiceBtn = findViewById(R.id.voiceBtn)
        barcodeBtn = findViewById(R.id.barcodeBtn)
        openCartBtn = findViewById(R.id.openCartBtn)
        recyclerView = findViewById(R.id.recyclerSearch)
        cartTotalText = findViewById(R.id.cartTotalText)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // TEMP DATA
        productList = mutableListOf(
            Product("1", "Milk", 20.0, "11111"),
            Product("2", "Sugar", 15.5, "22222"),
            Product("3", "Coffee", 40.0, "33333"),
            Product("4", "Rice", 50.0, "44444")
        )

        filteredList.addAll(productList)
        adapter = ProductAdapter(filteredList, this)
        recyclerView.adapter = adapter

        updateCartTotal()

        setupTextSearch()
        setupVoiceSearch()
        setupBarcodeSearch()

        openCartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    fun updateCartTotal() {
        cartTotalText.text = "Total: ${CartManager.total()} EGP"
    }

    private fun setupTextSearch() {
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
                val q = query.toString().lowercase()
                filteredList.clear()

                for (p in productList) {
                    if (p.name.lowercase().contains(q)) filteredList.add(p)
                }
                adapter.notifyDataSetChanged()
            }
        })
    }

//    private fun setupVoiceSearch() {
//        voiceBtn.setOnClickListener {
//            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
//            try {
//                startActivityForResult(intent, 200)
//            } catch (e: Exception) {
//                Toast.makeText(this, "Voice search not supported", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun setupVoiceSearch() {
        voiceBtn.setOnClickListener {

            // 1️⃣ Clear search first
            searchInput.setText("")
            filteredList.clear()
            filteredList.addAll(productList)
            adapter.notifyDataSetChanged()

            // 2️⃣ Immediately start voice recognition
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



    //    private fun setupBarcodeSearch() {
//        barcodeBtn.setOnClickListener {
//            val fakeBarcode = "33333"
//            searchInput.setText(fakeBarcode)
//            filteredList.clear()
//
//            for (p in productList) {
//                if (p.barcode == fakeBarcode) filteredList.add(p)
//            }
//
//            adapter.notifyDataSetChanged()
//            Toast.makeText(this, "Simulated barcode scan: $fakeBarcode", Toast.LENGTH_SHORT).show()
//        }
//    }
private fun setupBarcodeSearch() {
    barcodeBtn.setOnClickListener {
        val intent = Intent(this, BarcodeScannerActivity::class.java)
        barcodeLauncher.launch(intent)
    }
}


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200 && resultCode == RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            searchInput.setText(result?.get(0) ?: "")
        }
    }
}

///////////////////////////////////////////////////////////////
// PRODUCT MODEL
///////////////////////////////////////////////////////////////
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val barcode: String,
    var quantity: Int = 1
)

///////////////////////////////////////////////////////////////
// CART MANAGER
///////////////////////////////////////////////////////////////
object CartManager {
    val cartItems = mutableListOf<Product>()

    fun add(product: Product) {
        val existing = cartItems.find { it.id == product.id }
        if (existing != null) existing.quantity++
        else cartItems.add(product.copy(quantity = 1))
    }

    fun increase(p: Product) { p.quantity++ }
    fun decrease(p: Product) {
        p.quantity--
        if (p.quantity <= 0) cartItems.remove(p)
    }

    fun total(): Double = cartItems.sumOf { it.price * it.quantity }
}

///////////////////////////////////////////////////////////////
// PRODUCT LIST ADAPTER (Search screen)
///////////////////////////////////////////////////////////////
class ProductAdapter(
    private val items: MutableList<Product>,
    private val activity: SearchActivity
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.productName)
        val price: TextView = view.findViewById(R.id.productPrice)
        val addBtn: Button = view.findViewById(R.id.addToCartBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(v)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val p = items[position]

        holder.name.text = p.name
        holder.price.text = "${p.price} EGP"

        holder.addBtn.setOnClickListener {
            CartManager.add(p)
            Toast.makeText(holder.view.context, "${p.name} added to cart", Toast.LENGTH_SHORT).show()
            activity.updateCartTotal()
        }
    }

    override fun getItemCount(): Int = items.size
}

///////////////////////////////////////////////////////////////
// CART ADAPTER (with + / –)
///////////////////////////////////////////////////////////////
class CartAdapter(
    private val items: MutableList<Product>,
    private val activity: CartActivity
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.cartProductName)
        val price: TextView = view.findViewById(R.id.cartProductPrice)
        val qty: TextView = view.findViewById(R.id.cartQuantity)
        val plus: Button = view.findViewById(R.id.increaseBtn)
        val minus: Button = view.findViewById(R.id.decreaseBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(v)
    }

//    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
//        val p = items[position]
//
//        holder.name.text = p.name
//        holder.qty.text = p.quantity.toString()
//        holder.price.text = "${p.price * p.quantity} EGP"
//
//        holder.plus.setOnClickListener {
//            CartManager.increase(p)
//            notifyDataSetChanged()
//            activity.updateTotal()
//        }
//
//        holder.minus.setOnClickListener {
//            CartManager.decrease(p)
//            notifyDataSetChanged()
//            activity.updateTotal()
//        }
//    }
override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
    val p = items[position]

    holder.name.text = p.name
    holder.qty.text = p.quantity.toString()
    holder.price.text = "${p.price * p.quantity} EGP"

    holder.plus.setOnClickListener {
        CartManager.increase(p)
        activity.updateTotal()   // 🔥 resets order submitted
    }

    holder.minus.setOnClickListener {
        CartManager.decrease(p)
        activity.updateTotal()   // 🔥 resets order submitted
    }
}

    override fun getItemCount(): Int = items.size
}


