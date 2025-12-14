package com.example.e_commercemobapp.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val barcode: String,
    var quantity: Int = 1
)
