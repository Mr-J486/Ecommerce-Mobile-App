package com.example.e_commercemobapp.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobapp.R

class CartAdapter(
    private val items: MutableList<CartItem>,
    private val activity: CartActivity
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.cartProductName)
        val price: TextView = view.findViewById(R.id.cartProductPrice)
        val qty: TextView = view.findViewById(R.id.cartQuantity)
        val plus: TextView = view.findViewById(R.id.increaseBtn)
        val minus: TextView = view.findViewById(R.id.decreaseBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]

        holder.name.text = item.product.name
        holder.price.text = "${item.product.price} EGP"
        holder.qty.text = item.quantity.toString()

        holder.plus.setOnClickListener {
            CartManager.increase(item)
            holder.qty.text = item.quantity.toString()
            activity.updateTotal()
            notifyDataSetChanged()
        }

        holder.minus.setOnClickListener {
            CartManager.decrease(item)
            activity.updateTotal()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = items.size
}
