package com.example.splitwise.data

import com.example.splitwise.ui.util.MONTH

data class MonthData(val monthName : String) :Data {
    override fun getType(): Int {
        return MONTH
    }
}
