package com.example.splitwise.ui.holder

import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.data.CategoryAnalysisData
import com.example.splitwise.data.Data
import com.example.splitwise.databinding.PercentageWiseCategoryAnalysisBinding
import com.example.splitwise.ui.util.hide
import com.example.splitwise.ui.util.show
import com.example.splitwise.ui.util.showRupeeString

class CategoryWiseAnalysisHolder(val binding: PercentageWiseCategoryAnalysisBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

    fun setData(data: Data,isLastItem:Boolean) {

        val categoryData = data as CategoryAnalysisData
        binding.spendingType.text = categoryData.categoryName
        val totalPercent = (categoryData.totalExpenseInCategory * 100.0) / categoryData.totalExpense
        val formattedTotalPercent = String.format("%.1f", totalPercent)


        binding.totalExpense.text = showRupeeString( categoryData.totalExpenseInCategory.toString())
        binding.categoryPercentage.text = "$formattedTotalPercent%"


        val progressDrawable = binding.expenseProgress.progressDrawable as LayerDrawable

        val progressLayer = progressDrawable.findDrawableByLayerId(android.R.id.progress) as ClipDrawable
        val progressShape = progressLayer.drawable as GradientDrawable


        binding.expenseProgress.progressDrawable = progressDrawable

        if (totalPercent < 5) {
            binding.totalExpense.setTextColor(context.getColor(R.color.soft_green_color))
            progressShape.setColor(ContextCompat.getColor(context, R.color.soft_green_color))
        } else if (totalPercent.toInt() in 5..25) {
            binding.totalExpense.setTextColor(context.getColor(R.color.purple_500))
            progressShape.setColor(ContextCompat.getColor(context, R.color.purple_500))
        } else {
            binding.totalExpense.setTextColor(context.getColor(R.color.pausedColor))
            progressShape.setColor(ContextCompat.getColor(context, R.color.pausedColor))
        }


        binding.expenseProgress.progressDrawable = progressDrawable
        binding.expenseProgress.setProgress(totalPercent.toInt(),true)

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