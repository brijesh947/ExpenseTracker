package com.example.splitwise.data

import com.example.splitwise.ui.util.SIMPLE_TEXT

data class SimpleTextData(val name: String) : Data {
    override fun getType(): Int {
       return SIMPLE_TEXT
    }
}