package com.self.expensetracker.splitwise.data

import com.self.expensetracker.splitwise.ui.util.SHOPPING_FILTER_DATA

data class ExpenseFilterData(val currentMonth: Int,var filterSelected:Int) : Data {
    override fun getType(): Int {
        return SHOPPING_FILTER_DATA
    }
}
