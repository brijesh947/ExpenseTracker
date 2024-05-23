package com.example.splitwise.ui.holder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.data.Data
import com.example.splitwise.data.TimeWiseCategoryData
import com.example.splitwise.databinding.CategoryExpenseTimeWiseLayoutBinding
import com.example.splitwise.ui.util.showRupeeString
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