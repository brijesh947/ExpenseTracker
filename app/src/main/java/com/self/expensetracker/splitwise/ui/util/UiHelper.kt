package com.self.expensetracker.splitwise.ui.util

import android.view.View
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun showRupeeString(string: Long): String {
    return "\u20B9" + string
}

fun showRupeeString(string: String): String {
    return "\u20B9" + string
}

fun getCurrentMonthAndYear(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("MMMM_yyyy", Locale.getDefault())
    return "_" + dateFormat.format(calendar.time)
}