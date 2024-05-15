package com.example.splitwise.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.data.Data
import com.example.splitwise.data.MonthWiseProgressData
import com.example.splitwise.databinding.MonthWiseBalanceCardviewBinding
import com.example.splitwise.ui.util.showRupeeString

class MonthWiseProgressHolder(val binding: MonthWiseBalanceCardviewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setData(data: Data) {
        val monthWiseData = data as MonthWiseProgressData
        binding.balanceNumber.text = showRupeeString(monthWiseData.totalBudget - monthWiseData.totalExpense)

        val totalPercent = (monthWiseData.totalExpense * 100.0) / monthWiseData.totalBudget
        val formattedTotalPercent = String.format("%.1f", totalPercent)

        binding.expenseProgress.progress = totalPercent.toInt()

    }
}