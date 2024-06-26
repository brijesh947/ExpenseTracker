package com.example.splitwise.data

import com.example.splitwise.ui.util.SHOPPING_DATA

data class ShoppingData(
    var id: String,
    val shoppingName: String,
    val shoppingCategory: String,
    val totalAmount: String,
    val month :Int,
    val year :Int,
    val date:String
) : Data {
    override fun getType(): Int {
        return SHOPPING_DATA
    }
}
