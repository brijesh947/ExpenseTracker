package com.self.expensetracker.splitwise.data

import com.self.expensetracker.splitwise.ui.util.RECENT_TRANSACTION

data class RecentTransactionData(val name:String) :Data {
    override fun getType(): Int {
        return RECENT_TRANSACTION
    }
}
