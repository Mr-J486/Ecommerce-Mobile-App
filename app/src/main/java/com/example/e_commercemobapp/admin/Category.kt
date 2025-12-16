package com.example.e_commercemobapp.admin

class Category {
    var id: String? = null
        private set
    var name: String? = null
        private set
    var description: String? = null
        private set

    constructor()

    constructor(id: String?, name: String?, description: String?) {
        this.id = id
        this.name = name
        this.description = description
    }
}