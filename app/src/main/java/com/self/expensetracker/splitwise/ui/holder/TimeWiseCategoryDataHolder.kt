package com.self.expensetracker.splitwise.ui.holder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.data.TimeWiseCategoryData
import com.self.expensetracker.splitwise.databinding.CategoryExpenseTimeWiseLayoutBinding
import com.self.expensetracker.splitwise.ui.util.showRupeeString
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeWiseCategoryDataHolder(val binding: CategoryExpenseTimeWiseLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setData(data: Data){
        Log.d("fgwjedhasj", "in ViewHolder: ${data.toString()} ")
        val timeData = data as TimeWiseCategoryData
        val date = Date(timeData.time)
        val format = SimpleDateFormat("dd MMM h:mm a", Locale.getDefault())
        binding.time.text = format.format(date).replace("am", "AM").replace("pm", "PM")
        binding.spent.text  = showRupeeString(timeData.spent.toInt().toString())
    }
}