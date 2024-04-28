package com.example.splitwise.ui

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.CategoryFilterListener
import com.example.splitwise.R
import com.example.splitwise.data.Data
import com.example.splitwise.data.ExpenseCategoryData
import com.example.splitwise.databinding.ExpenseCategoryRecyclerItemBinding
import com.example.splitwise.ui.util.BEAUTY
import com.example.splitwise.ui.util.CLOTHING
import com.example.splitwise.ui.util.FOOD
import com.example.splitwise.ui.util.HEALTH
import com.example.splitwise.ui.util.MOVIE
import com.example.splitwise.ui.util.PETROL_PUMP
import com.example.splitwise.ui.util.RENT

class ExpenseCategoryHolder(val binding: ExpenseCategoryRecyclerItemBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

    fun setData(data: Data, filterListener: CategoryFilterListener<Int>, position: Int) {
        val categoryData = data as ExpenseCategoryData
        when (categoryData.getType()) {
            MOVIE -> {
                binding.categoryName.text = "Movie"
                binding.groupLogo.setImageResource(R.drawable.movie)
            }

            HEALTH -> {
                binding.categoryName.text = "Health"
                binding.groupLogo.setImageResource(R.drawable.health)
            }

            RENT -> {
                binding.categoryName.text = "Rent"
                binding.groupLogo.setImageResource(R.drawable.home)
            }

            BEAUTY -> {
                binding.categoryName.text = "Beauty"
                binding.groupLogo.setImageResource(R.drawable.beauty)
            }

            PETROL_PUMP -> {
                binding.categoryName.text = "Petrol"
                binding.groupLogo.setImageResource(R.drawable.petrol_pump)
            }

            FOOD -> {
                binding.categoryName.text = "Food"
                binding.groupLogo.setImageResource(R.drawable.food)
            }

            CLOTHING -> {
                binding.categoryName.text = "Clothing"
                binding.groupLogo.setImageResource(R.drawable.clothing)
            }

            else -> {
                binding.categoryName.text = "Other"
                binding.groupLogo.setImageResource(R.drawable.shopping)
            }

        }
        if (categoryData.isSelected) {

            binding.categoryName.setTextColor(context.resources.getColor(R.color.text_cta_color))
            val drawable = ContextCompat.getDrawable(context, R.drawable.only_stroke_text_cta_color_33dp)
            binding.logoCard.foreground = drawable

        } else {
            binding.categoryName.setTextColor(context.resources.getColor(R.color.primary_txt))
            val drawable = ContextCompat.getDrawable(context, R.drawable.only_stroke_ce_low_contrast_fg_33dp)
            binding.logoCard.foreground = drawable

        }
        

        binding.root.setOnClickListener { filterListener.selectedFilter(categoryData.getType(),position) }
    }
}