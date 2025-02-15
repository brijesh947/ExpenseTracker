package com.self.expensetracker.splitwise

interface CategoryFilterListener<T : Any> {

    fun selectedFilter(categoryName: String, categoryType: T, position: Int)
}