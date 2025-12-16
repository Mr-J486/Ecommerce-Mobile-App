package com.example.e_commercemobapp.model

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val categoryId: String = "",
    val barcode: String = ""
)

