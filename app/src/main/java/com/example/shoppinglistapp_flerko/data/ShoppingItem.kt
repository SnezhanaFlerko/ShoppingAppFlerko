package com.example.shoppinglistapp_flerko.data

import java.util.*

data class ShoppingItem(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var quantity: Int = 1,
    var price: Double = 0.0,
    var bought: Boolean = false,
    var reminderTime: Long? = null
)