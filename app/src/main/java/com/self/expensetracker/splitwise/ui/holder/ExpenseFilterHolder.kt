package com.self.expensetracker.splitwise.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.data.ExpenseFilterData
import com.self.expensetracker.splitwise.databinding.ExpesnseFilterLayoutBinding
import com.self.expensetracker.splitwise.ui.ExpenseFilterListener
import com.self.expensetracker.splitwise.ui.util.CURR_MONTH_FILTER
import com.self.expensetracker.splitwise.ui.util.NO_FILTER
import com.self.expensetracker.splitwise.ui.util.PREV_MONTH_FILTER
import java.util.Calendar

class ExpenseFilterHolder(val binding: ExpesnseFilterLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setData(data: Data, callback: ExpenseFilterListener) {

        val filterData = data as ExpenseFilterData
        val prevMonth = getPrevMonth(filterData.currentMonth)

        binding.currentMonth.text = "All"
        binding.prevMonth.text = getMonthText(filterData.currentMonth)
        binding.all.text = getMonthText(prevMonth)

        when (filterData.filterSelected) {
            NO_FILTER -> {
                binding.currentMonth.isChecked = true
            }

            PREV_MONTH_FILTER -> {
                binding.all.isChecked = true
            }

            CURR_MONTH_FILTER -> {
                binding.prevMonth.isChecked = true
            }

        }

        binding.currentMonth.setOnClickListener {
            callback.selectedFilter(NO_FILTER,-1)
        }

        binding.prevMonth.setOnClickListener {
            callback.selectedFilter(CURR_MONTH_FILTER,filterData.currentMonth)
        }

        binding.all.setOnClickListener {
            callback.selectedFilter(PREV_MONTH_FILTER,prevMonth)
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