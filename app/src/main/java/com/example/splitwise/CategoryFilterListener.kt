package com.example.splitwise

interface CategoryFilterListener<T : Any> {

    fun selectedFilter(categoryName: String, categoryType: T, position: Int)
}