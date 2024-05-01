package com.example.splitwise.data

import com.example.splitwise.ui.util.SHOPPING_FILTER_DATA

data class ExpenseFilterData(val currentMonth: Int) : Data {
    override fun getType(): Int {
        return SHOPPING_FILTER_DATA
    }
}
