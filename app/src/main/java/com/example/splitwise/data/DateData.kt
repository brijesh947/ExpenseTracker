package com.example.splitwise.data

import com.example.splitwise.ui.util.DATE_TYPE

data class DateData(val date: String, val currentDate: Int, val month: Int, val year: Int) : Data {
    override fun getType(): Int {
        return DATE_TYPE
    }
}
