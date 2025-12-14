package com.example.e_commercemobapp.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobapp.R
import com.example.e_commercemobapp.model.Product

class CartAdapter(
    private val items: MutableList<Product>,
    private val activity: CartActivity
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.cartProductName)
        val price: TextView = view.findViewById(R.id.cartProductPrice)
        val qty: TextView = view.findViewById(R.id.cartQuantity)
        val plus: Button = view.findViewById(R.id.increaseBtn)
        val minus: Button = view.findViewById(R.id.decreaseBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = items[position]

        holder.name.text = product.name
        holder.qty.text = product.quantity.toString()
        holder.price.text = "${product.price * product.quantity} EGP"

        holder.plus.setOnClickListener {
            CartManager.increase(product)
            notifyItemChanged(position)
            activity.updateTotal()
        }

        holder.minus.setOnClickListener {
            CartManager.decrease(product)
            notifyDataSetChanged()
            activity.updateTotal()
        }
    }

    override fun getItemCount(): Int = items.size
}
