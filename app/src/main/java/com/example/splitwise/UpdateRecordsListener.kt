package com.example.splitwise

import com.example.splitwise.data.ShoppingData

interface UpdateRecordsListener {

    fun updateRecord(data: ShoppingData, diff:Double, position: Int)
    fun deleteRecord(data: ShoppingData,position: Int)

}