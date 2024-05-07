package com.example.splitwise.data

import com.example.splitwise.ui.util.PERCENTAGE_WISE_ANALYSIS

data class CategoryAnalysisData(val categoryName:String, val totalExpenseInCategory: Long, val totalExpense: Long) : Data {
    override fun getType(): Int {
        return PERCENTAGE_WISE_ANALYSIS
    }
}
