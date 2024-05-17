package com.example.splitwise.data

import com.example.splitwise.ui.util.TOTAL_SPENDING_CARD

data class MonthWiseProgressData(var totalBudget: Long, var totalExpense: Long) : Data {
    override fun getType(): Int {
        return TOTAL_SPENDING_CARD
    }
}
