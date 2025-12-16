package com.example.e_commercemobapp.cart

import com.example.e_commercemobapp.model.Product

object CartManager {

    private val cartItems = mutableListOf<CartItem>()

    fun add(product: Product) {
        val existing = cartItems.find { it.product.id == product.id }

        if (existing != null) {
            existing.quantity++
        } else {
            cartItems.add(CartItem(product, 1))
        }
    }

    fun increase(item: CartItem) {
        item.quantity++
    }

    fun decrease(item: CartItem) {
        item.quantity--
        if (item.quantity <= 0) cartItems.remove(item)
    }

    fun remove(item: CartItem) {
        cartItems.remove(item)
    }

    fun getItems(): MutableList<CartItem> = cartItems

    fun total(): Double {
        return cartItems.sumOf { it.product.price * it.quantity }
    }

    fun clear() {
        cartItems.clear()
    }
}
