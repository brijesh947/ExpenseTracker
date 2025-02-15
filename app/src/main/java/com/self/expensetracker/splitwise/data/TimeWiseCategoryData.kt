package com.self.expensetracker.splitwise.data

import com.self.expensetracker.splitwise.ui.util.TIME_WISE_CATEGORY_DATA

data class TimeWiseCategoryData(val time :Long, val spent:Double):Data {
    override fun getType(): Int {
        return TIME_WISE_CATEGORY_DATA
    }
}
