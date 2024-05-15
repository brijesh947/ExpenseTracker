package com.example.splitwise.data

import com.example.splitwise.ui.util.RECENT_TRANSACTION

data class RecentTransactionData(val name:String) :Data {
    override fun getType(): Int {
        return RECENT_TRANSACTION
    }
}
