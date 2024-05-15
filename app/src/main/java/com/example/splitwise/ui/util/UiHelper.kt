package com.example.splitwise.ui.util

import android.view.View


fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}
fun showRupeeString(string: Long): String {
    return "\u20B9" + string
}
fun Int.showRupeeString(string: Int): String {
    return "\u20B9" + string
}