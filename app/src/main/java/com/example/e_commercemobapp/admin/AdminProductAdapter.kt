package com.example.e_commercemobapp.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobapp.R
import com.example.e_commercemobapp.model.Product

class AdminProductAdapter(
    private val context: Context,
    private val products: MutableList<Product>,
    private val onClick: (Product) -> Unit
) : RecyclerView.Adapter<AdminProductAdapter.AdminProductViewHolder>() {

    inner class AdminProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.productNameText)
        val category: TextView = view.findViewById(R.id.productCategoryText)
        val price: TextView = view.findViewById(R.id.productPriceText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_item_product, parent, false)
        return AdminProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminProductViewHolder, position: Int) {
        val p = products[position]

        holder.name.text = p.name
        holder.category.text = p.categoryId
        holder.price.text = "${p.price} EGP"

        holder.itemView.setOnClickListener {
            onClick(p)
        }
    }

    override fun getItemCount(): Int = products.size
}
