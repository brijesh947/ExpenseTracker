package com.self.expensetracker.splitwise.ui.fragment

import java.util.Calendar

class MonthManager private constructor() {


    private var currMonth: Int = -1
    private var currYear: Int = -1


    companion object {
        @Volatile
        private var instance: MonthManager? = null

        fun getInstance(): MonthManager {
            if (instance == null) synchronized(this) {
                instance = MonthManager()
            }
            return instance!!
        }
    }

    fun setMonthAndYear(month: Int, year: Int) {
        currMonth = month
        currYear = year
    }

    fun getCurrentYear(): Int {
        if (currYear == -1) {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
            }
            currMonth = calendar.get(Calendar.MONTH)
            currYear = calendar.get(Calendar.YEAR)
        }

        return currYear
    }

    fun getCurrentMonth(): Int {
        if (currMonth == -1) {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
            }
            currMonth = calendar.get(Calendar.MONTH)
            currYear = calendar.get(Calendar.YEAR)
        }

        return currMonth
    }

    fun resetMonthAndYear() {
        currMonth = -1
        currYear = -1
    }
}