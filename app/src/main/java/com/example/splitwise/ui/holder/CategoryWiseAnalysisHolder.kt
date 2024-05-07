package com.example.splitwise.ui.holder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.data.CategoryAnalysisData
import com.example.splitwise.data.Data
import com.example.splitwise.databinding.PercentageWiseCategoryAnalysisBinding
import com.example.splitwise.ui.util.hide
import com.example.splitwise.ui.util.show

class CategoryWiseAnalysisHolder(val binding: PercentageWiseCategoryAnalysisBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

    fun setData(data: Data,isLastItem:Boolean) {

        val categoryData = data as CategoryAnalysisData
        binding.spendingType.text = categoryData.categoryName
        val totalPercent = (categoryData.totalExpenseInCategory * 100.0) / categoryData.totalExpense
        val formattedTotalPercent = String.format("%.1f", totalPercent)

        binding.expenseProgress.progress = totalPercent.toInt()
        binding.totalExpense.text = categoryData.totalExpenseInCategory.toString()
        binding.categoryPercentage.text = "$formattedTotalPercent%"
        if (isLastItem)
            binding.seprator.hide()
        else
            binding.seprator.show()
        setImageIcon(categoryData)

    }

    private fun setImageIcon(categoryData: CategoryAnalysisData) {

        when (categoryData.categoryName) {
            "MOVIE" ->
                binding.groupLogo.setImageResource(R.drawable.movie)

            "CLOTHING" ->
                binding.groupLogo.setImageResource(R.drawable.clothing)

            "BEAUTY" ->
                binding.groupLogo.setImageResource(R.drawable.beauty)

            "FOOD" ->
                binding.groupLogo.setImageResource(R.drawable.food)

            "HEALTH" ->
                binding.groupLogo.setImageResource(R.drawable.health)

            "RENT" ->
                binding.groupLogo.setImageResource(R.drawable.home)

            "PETROL_PUMP" ->
                binding.groupLogo.setImageResource(R.drawable.petrol_pump)

            "BIKE" ->
                binding.groupLogo.setImageResource(R.drawable.bike)

            "TRANSPORT" ->
                binding.groupLogo.setImageResource(R.drawable.car)

            "DONATE" ->
                binding.groupLogo.setImageResource(R.drawable.donate)

            "SPORTS" ->
                binding.groupLogo.setImageResource(R.drawable.sports)

            "MOBILE" ->
                binding.groupLogo.setImageResource(R.drawable.mobile_recharge)

            else ->
                binding.groupLogo.setImageResource(R.drawable.shopping)
        }


    }

}