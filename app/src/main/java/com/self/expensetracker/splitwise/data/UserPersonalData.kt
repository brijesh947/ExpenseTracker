package com.self.expensetracker.splitwise.data


data class UserPersonalData(
    val name: String,
    val email: String,
    var firstName: String
):Data {
    override fun getType(): Int {
       return -1
    }
}
