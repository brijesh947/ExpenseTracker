package com.self.expensetracker.splitwise.data

import com.self.expensetracker.splitwise.ui.util.SIMPLE_TEXT

data class SimpleTextData(val name: String, var addCreateCategory: Boolean = false) : Data {
    override fun getType(): Int {
       return SIMPLE_TEXT
    }
}