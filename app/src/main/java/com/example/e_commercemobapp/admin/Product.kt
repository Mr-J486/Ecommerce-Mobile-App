package com.example.e_commercemobapp.admin

class Product {
    var id: String? = null
        private set
    var name: String? = null
        private set
    var description: String? = null
        private set
    var price: Double = 0.0
        private set
    var quantity: Int = 0
        private set
    var categoryName: String? = null
        private set
    var imageUrl: String? = null
        private set

    constructor()

    constructor(
        id: String?,
        name: String?,
        description: String?,
        price: Double,
        quantity: Int,
        categoryName: String?,
        imageUrl: String?
    ) {
        this.id = id
        this.name = name
        this.description = description
        this.price = price
        this.quantity = quantity
        this.categoryName = categoryName
        this.imageUrl = imageUrl
    }
}