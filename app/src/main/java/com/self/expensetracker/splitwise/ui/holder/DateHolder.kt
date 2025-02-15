package com.self.expensetracker.splitwise.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.data.DateData
import com.self.expensetracker.splitwise.databinding.DateLayoutBinding

class DateHolder(val binding: DateLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setData(data: Data) {
        binding.date.text = (data as DateData).date
    }
}