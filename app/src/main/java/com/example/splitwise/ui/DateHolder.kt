package com.example.splitwise.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.data.Data
import com.example.splitwise.data.DateData
import com.example.splitwise.databinding.DateLayoutBinding

class DateHolder(val binding: DateLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setData(data: Data) {
        binding.date.text = (data as DateData).date
    }
}