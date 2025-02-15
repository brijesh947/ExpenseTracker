package com.self.expensetracker.splitwise

interface FirebaseCallback<T : Any> {
    fun isSuccess(result: T)
    fun isFailed(reason: String)
}