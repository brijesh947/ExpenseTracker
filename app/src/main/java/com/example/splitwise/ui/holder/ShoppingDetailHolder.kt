package com.example.splitwise.ui.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.data.Data
import com.example.splitwise.data.ShoppingData
import com.example.splitwise.databinding.SpendDetailLayoutBinding

class ShoppingDetailHolder(val binding: SpendDetailLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun setData(data: Data) {
        val newData = data as ShoppingData
        binding.spendingType.text = data.shoppingName
        binding.totalExpense.text = data.totalAmount
        when (data.shoppingCategory.toUpperCase()) {
            "RENT" -> {
                binding.groupLogo.setImageResource(R.drawable.home)
            }

            "HEALTH" -> {
                binding.groupLogo.setImageResource(R.drawable.health)
            }

            "FOOD" -> {
                binding.groupLogo.setImageResource(R.drawable.food)
            }

            "MOVIE" -> {
                binding.groupLogo.setImageResource(R.drawable.movie)
            }

            "CLOTHING" -> {
                binding.groupLogo.setImageResource(R.drawable.clothing)
            }

            "PETROL_PUMP" -> {
                binding.groupLogo.setImageResource(R.drawable.petrol_pump)
            }

            "BEAUTY" -> {
                binding.groupLogo.setImageResource(R.drawable.beauty)
            }

            "BIKE" -> {
                binding.groupLogo.setImageResource(R.drawable.bike)
            }

            "SPORTS" -> {

                binding.groupLogo.setImageResource(R.drawable.sports)
            }

            "MOBILE" -> {
                binding.groupLogo.setImageResource(R.drawable.mobile_recharge)
            }

            "TRANSPORT" -> {
                binding.groupLogo.setImageResource(R.drawable.car)
            }

            "DONATE" -> {
                binding.groupLogo.setImageResource(R.drawable.donate)
            }

            else -> binding.groupLogo.setImageResource(R.drawable.shopping)
        }
    }
}