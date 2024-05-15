package com.example.splitwise.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.data.Data
import com.example.splitwise.data.RecentTransactionData
import com.example.splitwise.databinding.RecentTransactionLayoutBinding

class RecentTransactionHolder(val binding: RecentTransactionLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setData(data: Data) {
        binding.name.text = (data as RecentTransactionData).name
    }
}