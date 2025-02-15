package com.self.expensetracker.splitwise.data

import com.self.expensetracker.splitwise.ui.util.DATE_TYPE

data class DateData(val date: String, val currentDate: Int, val month: Int, val year: Int) : Data {
    override fun getType(): Int {
        return DATE_TYPE
    }
}
