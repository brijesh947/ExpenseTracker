package com.example.splitwise.data

data class ExpenseCategoryData(val categoryType: Int,var isSelected:Boolean) : Data {
    override fun getType(): Int {
        return categoryType
    }
}
