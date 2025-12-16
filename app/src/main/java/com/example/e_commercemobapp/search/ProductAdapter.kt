package com.example.e_commercemobapp.search.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commercemobapp.R
import com.example.e_commercemobapp.cart.CartManager
import com.example.e_commercemobapp.model.Product
import com.example.e_commercemobapp.search.SearchActivity

class ProductAdapter(
    private val items: MutableList<Product>,
    private val categoryNames: Map<String, String>,
    private val activity: SearchActivity
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.productImage)
        val name: TextView = view.findViewById(R.id.productName)
        val category: TextView = view.findViewById(R.id.productCategory)
        val price: TextView = view.findViewById(R.id.productPrice)
        val addBtn: Button = view.findViewById(R.id.addToCartBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val p = items[position]

        // 🔥 Load product image from Firestore "imageUrl"
        Glide.with(holder.itemView.context)
            .load(p.imageUrl)
            .error(R.drawable.ic_broken_image) // Optional fallback
            .into(holder.image)

        holder.name.text = p.name
        holder.category.text = categoryNames[p.categoryId] ?: "Unknown"
        holder.price.text = "${p.price} EGP"

        // Add to cart
        holder.addBtn.setOnClickListener {
            CartManager.add(p)
            activity.updateCartTotal()
            Toast.makeText(holder.itemView.context, "${p.name} added", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = items.size
}
