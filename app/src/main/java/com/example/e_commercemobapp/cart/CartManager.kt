package com.example.e_commercemobapp.cart

import com.example.e_commercemobapp.model.Product

object CartManager {

    private val cartItems = mutableListOf<Product>()

    fun add(product: Product) {
        val existing = cartItems.find { it.id == product.id }
        if (existing != null) existing.quantity++
        else cartItems.add(product.copy(quantity = 1))
    }

    fun increase(p: Product) {
        p.quantity++
    }

    fun decrease(p: Product) {
        p.quantity--
        if (p.quantity <= 0) cartItems.remove(p)
    }

    fun total(): Double = cartItems.sumOf { it.price * it.quantity }

    fun clear() = cartItems.clear()

    fun getItems(): MutableList<Product> = cartItems
}
