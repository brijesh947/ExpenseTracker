package com.example.splitwise.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.data.Data
import com.example.splitwise.data.MonthData
import com.example.splitwise.databinding.MonthFilterLayoutBinding

class MonthHolder(val binding: MonthFilterLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setData(data: Data) {
        binding.monthName.text = (data as MonthData).monthName
    }
}