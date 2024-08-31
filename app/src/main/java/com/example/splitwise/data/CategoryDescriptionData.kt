package com.example.splitwise.data

data class CategoryDescriptionData(val categoryName:String,val categoryDestype: Int) : Data {
    override fun getType(): Int {
        return categoryDestype
    }
}
