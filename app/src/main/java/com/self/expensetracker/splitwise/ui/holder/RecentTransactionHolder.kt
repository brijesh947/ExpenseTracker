package com.self.expensetracker.splitwise.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.data.RecentTransactionData
import com.self.expensetracker.splitwise.databinding.RecentTransactionLayoutBinding

class RecentTransactionHolder(val binding: RecentTransactionLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setData(data: Data) {
        binding.name.text = (data as RecentTransactionData).name
    }
}