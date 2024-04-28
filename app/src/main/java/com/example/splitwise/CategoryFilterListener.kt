package com.example.splitwise

interface CategoryFilterListener<T : Any> {

    fun selectedFilter(categoryType: T,position:Int)
}