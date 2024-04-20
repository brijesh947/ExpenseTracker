package com.example.splitwise

import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.data.Data
import com.example.splitwise.data.ShoppingData
import com.example.splitwise.databinding.SpendDetailLayoutBinding

class ShoppingDetailHolder(val binding: SpendDetailLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setData(data: Data) {
        val newData = data as ShoppingData
        binding.spendingType.text = data.shoppingName
        binding.totalExpense.text = data.totalAmount
        when (data.shoppingCategory) {
            "Rent" -> {
                binding.groupLogo.setImageResource(R.drawable.home)
            }
            "Health" -> {
                binding.groupLogo.setImageResource(R.drawable.health)
            }
            "Food" -> {
                binding.groupLogo.setImageResource(R.drawable.food)
            }
            else -> binding.groupLogo.setImageResource(R.drawable.shopping)
        }
    }
}