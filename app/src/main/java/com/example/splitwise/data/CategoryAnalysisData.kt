package com.example.splitwise.data

import com.example.splitwise.ui.util.PERCENTAGE_WISE_ANALYSIS

data class CategoryAnalysisData(
    val categoryType:String,
    val categoryName: String,
    val totalExpenseInCategory: List<Pair<Long, Double>>,
    val totalExpense: Long,
    var finalExpenseInCategory: Double = 0.0
) : Data {
    override fun getType(): Int {
        return PERCENTAGE_WISE_ANALYSIS
    }
}
