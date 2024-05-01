package com.example.splitwise.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.data.Data
import com.example.splitwise.data.ExpenseFilterData
import com.example.splitwise.databinding.ExpesnseFilterLayoutBinding
import com.example.splitwise.ui.util.CURR_MONTH_FILTER
import com.example.splitwise.ui.util.NO_FILTER
import com.example.splitwise.ui.util.PREV_MONTH_FILTER
import java.util.Calendar

class ExpenseFilterHolder(val binding: ExpesnseFilterLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setData(data: Data, callback: ExpenseFilterListener) {

        val filterData = data as ExpenseFilterData
        val prevMonth = getPrevMonth(filterData.currentMonth)

        binding.currentMonth.text = getMonthText(filterData.currentMonth)
        binding.prevMonth.text = getMonthText(prevMonth)
        binding.all.text = "ALL"

        binding.currentMonth.setOnClickListener {
            callback.selectedFilter(CURR_MONTH_FILTER,filterData.currentMonth)
        }

        binding.prevMonth.setOnClickListener {
            callback.selectedFilter(PREV_MONTH_FILTER,prevMonth)
        }

        binding.all.setOnClickListener {
            callback.selectedFilter(NO_FILTER,-1)
        }

    }

    private fun getMonthText(currentMonth: Int): String {

        when (currentMonth) {

            Calendar.JANUARY -> {
                return "JANUARY"
            }

            Calendar.FEBRUARY -> {
                return "FEBRUARY"
            }

            Calendar.MARCH -> {
                return "MARCH"
            }

            Calendar.APRIL -> {
                return "APRIL"
            }

            Calendar.MAY -> {
                return "MAY"
            }

            Calendar.JUNE -> {
                return "JUNE"
            }

            Calendar.JULY -> {
                return "JULY"
            }

            Calendar.AUGUST -> {
                return "AUGUST"
            }

            Calendar.SEPTEMBER -> {
                return "SEPTEMBER"
            }

            Calendar.OCTOBER -> {
                return "OCTOBER"
            }

            Calendar.NOVEMBER -> {
                return "NOVEMBER"
            }

            Calendar.DECEMBER -> {
                return "DECEMBER"
            }

        }
        return ""

    }

    private fun getPrevMonth(month: Int): Int {
        if (month > 0)
            return month - 1
        return 11
    }

}