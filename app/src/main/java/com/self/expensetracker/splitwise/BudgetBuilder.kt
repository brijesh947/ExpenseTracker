package com.self.expensetracker.splitwise

import com.self.expensetracker.splitwise.data.CurrBudgetData

interface BudgetBuilder {
    fun setBudget(data : CurrBudgetData,budget: String, position: Int)

}