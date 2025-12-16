package com.example.e_commercemobapp.cart

import com.example.e_commercemobapp.model.Product

data class CartItem(
    val product: Product,
    var quantity: Int = 1
)
