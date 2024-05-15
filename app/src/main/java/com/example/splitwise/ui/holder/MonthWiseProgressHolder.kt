package com.example.splitwise.ui.holder

import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.data.Data
import com.example.splitwise.data.MonthWiseProgressData
import com.example.splitwise.databinding.MonthWiseBalanceCardviewBinding
import com.example.splitwise.ui.util.showRupeeString

class MonthWiseProgressHolder(val binding: MonthWiseBalanceCardviewBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

    fun setData(data: Data, position: Int) {
        val monthWiseData = data as MonthWiseProgressData
        if (position == 0) {
            binding.balanceText.text = "Available Balance"
            binding.balanceNumber.text =
                showRupeeString(monthWiseData.totalBudget - monthWiseData.totalExpense)
            val totalPercent =
                ((monthWiseData.totalBudget - monthWiseData.totalExpense) * 100.0) / monthWiseData.totalBudget
            val formattedTotalPercent = String.format("%.1f", totalPercent)
            val progressDrawable = binding.expenseProgress.progressDrawable as LayerDrawable
            val progressLayer =
                progressDrawable.findDrawableByLayerId(android.R.id.progress) as ClipDrawable
            val progressShape = progressLayer.drawable as GradientDrawable
            if (totalPercent.toInt() > 80) {
                progressShape.setColor(ContextCompat.getColor(context, R.color.soft_green_color))
                binding.balanceNumber.setTextColor(context.getColor(R.color.soft_green_color))
            } else if (totalPercent.toInt() in 31..79) {
                progressShape.setColor(ContextCompat.getColor(context, R.color.purple_500))
                binding.balanceNumber.setTextColor(context.getColor(R.color.purple_500))
            } else {
                progressShape.setColor(ContextCompat.getColor(context, R.color.pausedColor))
                binding.balanceNumber.setTextColor(context.getColor(R.color.pausedColor))
            }

            binding.expenseProgress.progressDrawable = progressDrawable

            binding.expenseProgress.progress = totalPercent.toInt()
        } else {
            binding.balanceText.text = "Total Expense"
            binding.balanceNumber.text = showRupeeString(monthWiseData.totalExpense)
            val totalPercent = (monthWiseData.totalExpense * 100.0) / monthWiseData.totalBudget
            val formattedTotalPercent = String.format("%.1f", totalPercent)
            val progressDrawable = binding.expenseProgress.progressDrawable as LayerDrawable
            val progressLayer =
                progressDrawable.findDrawableByLayerId(android.R.id.progress) as ClipDrawable
            val progressShape = progressLayer.drawable as GradientDrawable

            if (totalPercent.toInt() < 30) {
                progressShape.setColor(ContextCompat.getColor(context, R.color.soft_green_color))
                binding.balanceNumber.setTextColor(context.getColor(R.color.soft_green_color))
            } else if (totalPercent.toInt() in 31..79) {
                progressShape.setColor(ContextCompat.getColor(context, R.color.purple_500))
                binding.balanceNumber.setTextColor(context.getColor(R.color.purple_500))
            } else {
                progressShape.setColor(ContextCompat.getColor(context, R.color.pausedColor))
                binding.balanceNumber.setTextColor(context.getColor(R.color.pausedColor))
            }
            binding.expenseProgress.progressDrawable = progressDrawable
            binding.expenseProgress.progress = totalPercent.toInt()
        }

    }
}