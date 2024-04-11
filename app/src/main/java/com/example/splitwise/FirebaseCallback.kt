package com.example.splitwise

interface FirebaseCallback<T : Any> {
    fun isSuccess(result: T)
    fun isFailed(reason: String)
}