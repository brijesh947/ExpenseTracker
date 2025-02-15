package com.self.expensetracker.splitwise

import com.self.expensetracker.splitwise.data.ShoppingData

interface UpdateRecordsListener {

    fun updateRecord(data: ShoppingData, diff:Double, position: Int)
    fun deleteRecord(data: ShoppingData,position: Int)

}