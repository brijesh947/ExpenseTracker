package com.example.splitwise.data

import com.example.splitwise.ui.util.SHOPPING_DATA

data class ShoppingData(
    var id: String,
    var shoppingName: String,
    var shoppingCategory: String,
    var shoppingCategoryType: String,
    var totalAmount: String,
    val month :Int,
    val year :Int,
    val date:String,
    var comment :String = ""
) : Data {
    override fun getType(): Int {
        return SHOPPING_DATA
    }
}
