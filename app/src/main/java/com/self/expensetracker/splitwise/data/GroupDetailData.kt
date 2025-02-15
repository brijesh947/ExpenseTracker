package com.self.expensetracker.splitwise.data

import com.self.expensetracker.splitwise.ui.util.GROUP_DATA

data class GroupDetailData(
    var id : String,
    val groupName: String,
    val groupType: String,
    val totalExpense: String
):Data {
    override fun getType(): Int {
       return GROUP_DATA
    }
}
