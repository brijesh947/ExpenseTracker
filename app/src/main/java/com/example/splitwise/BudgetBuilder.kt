package com.example.splitwise

import com.example.splitwise.data.CurrBudgetData

interface BudgetBuilder {
    fun setBudget(data : CurrBudgetData,budget: String, position: Int)

}