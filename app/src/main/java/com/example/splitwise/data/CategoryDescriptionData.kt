package com.example.splitwise.data

data class CategoryDescriptionData(val categoryDestype: Int) : Data {
    override fun getType(): Int {
        return categoryDestype
    }
}
