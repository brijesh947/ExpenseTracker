package com.example.splitwise.data

import com.example.splitwise.ui.util.CURR_BUDGET

data class CurrBudgetData(val name: String, var totalBudget: Long, val totalExpense: Double) : Data {

    override fun getType(): Int {
        return CURR_BUDGET
    }
}