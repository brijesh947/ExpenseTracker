package com.example.splitwise.data

data class ExpenseCategoryData(val categoryName:String,val categoryType: Int,var isSelected:Boolean) : CategoryTypeData {

    override fun getCategoryTypeName(): String {
        return categoryName
    }

    override fun getType(): Int {
        return categoryType
    }
}
