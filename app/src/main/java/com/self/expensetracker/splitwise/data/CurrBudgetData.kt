package com.self.expensetracker.splitwise.data

import com.self.expensetracker.splitwise.ui.util.CURR_BUDGET

data class CurrBudgetData(val name: String, var totalBudget: Long, val totalExpense: Double) : Data {

    override fun getType(): Int {
        return CURR_BUDGET
    }
}