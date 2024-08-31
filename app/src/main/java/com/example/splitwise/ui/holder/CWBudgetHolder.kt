package com.example.splitwise.ui.holder

import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.BudgetBuilder
import com.example.splitwise.R
import com.example.splitwise.data.CurrBudgetData
import com.example.splitwise.data.Data
import com.example.splitwise.databinding.BudgetBuilderLayoutBinding
import com.example.splitwise.databinding.CategoryWiseBudgetLayoutBinding
import com.example.splitwise.ui.util.hide
import com.example.splitwise.ui.util.show
import com.example.splitwise.ui.util.showRupeeString
import com.google.android.material.bottomsheet.BottomSheetDialog

class CWBudgetHolder(val binding: CategoryWiseBudgetLayoutBinding, val context: Context,val listener:BudgetBuilder) : RecyclerView.ViewHolder(binding.root) {

    fun setData(data: Data, isLastItem: Boolean, position: Int) {

        val budgetData = data as CurrBudgetData

        binding.spendingType.text = budgetData.name
        binding.limit.text = showRupeeString(budgetData.totalBudget)
        binding.spent.text = showRupeeString(budgetData.totalExpense.toLong())
        binding.remaining.text = showRupeeString((budgetData.totalBudget - budgetData.totalExpense).toLong())

        val expensePercentage = (100 * budgetData.totalExpense.toLong()) / budgetData.totalBudget
        val remainingPercentage = 100 - expensePercentage

        val progressDrawable = binding.expenseProgress.progressDrawable as LayerDrawable

        val progressLayer = progressDrawable.findDrawableByLayerId(android.R.id.progress) as ClipDrawable
        val progressShape = progressLayer.drawable as GradientDrawable


        binding.expenseProgress.progressDrawable = progressDrawable




        if (expensePercentage < 40) {
            binding.spent.setTextColor(context.getColor(R.color.soft_green_color))
            progressShape.setColor(ContextCompat.getColor(context, R.color.soft_green_color))
        }
        else if (expensePercentage in 40..79) {
            binding.spent.setTextColor(context.getColor(R.color.purple_500))
            progressShape.setColor(ContextCompat.getColor(context, R.color.purple_500))
        }
        else {
            binding.spent.setTextColor(context.getColor(R.color.pausedColor))
            progressShape.setColor(ContextCompat.getColor(context, R.color.pausedColor))
        }

        binding.expenseProgress.progressDrawable = progressDrawable


        if (remainingPercentage >= 80)
            binding.remaining.setTextColor(context.getColor(R.color.soft_green_color))
        else if (remainingPercentage in 40..79)
            binding.remaining.setTextColor(context.getColor(R.color.purple_500))
        else
            binding.remaining.setTextColor(context.getColor(R.color.pausedColor))


        if (budgetData.totalExpense >= budgetData.totalBudget)
            binding.limitExceeded.show()
        else
            binding.limitExceeded.hide()

        if (position == 1 && !isLastItem)
            binding.seprator.visibility = View.INVISIBLE
        else {
            if (isLastItem)
                binding.seprator.hide()
            else
                binding.seprator.show()
        }

        binding.expenseProgress.setProgress(expensePercentage.toInt(),true)

        val regex = ".*\\d{4}\$".toRegex()

        if (!regex.matches(budgetData.name))
            setImageIcon(budgetData.name)
        else
            binding.groupLogo.setImageResource(R.drawable.month_icon)

        binding.editButton.setOnClickListener {
            openBudgetBuilderDialog(budgetData)
        }

    }

    private fun openBudgetBuilderDialog(budgetData: CurrBudgetData) {
        val dialogView = DataBindingUtil.inflate<BudgetBuilderLayoutBinding>(
            LayoutInflater.from(context), R.layout.budget_builder_layout, null, false
        )
        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialogStyle)
        dialogView.done.setOnClickListener {
            if (verifyInput(dialogView)) {
                budgetData.totalBudget = dialogView.shoppingPrice.text.toString().toLong()
                listener.setBudget(budgetData, dialogView.shoppingPrice.text.toString(), position)
            }
            dialog.dismiss()

        }
        dialogView.categoryName.setText(budgetData.name)
        dialog.setContentView(dialogView.root)
        dialog.show()
    }

    private fun verifyInput(dialogView: BudgetBuilderLayoutBinding): Boolean {

        if (dialogView.shoppingPrice.text.isEmpty()) {
            dialogView.shoppingPrice.error = "Budget price can't be Empty"
            return false
        }

        return true
    }

    private fun setImageIcon(name: String) {

        when (name) {
            "MOVIE" ->
                binding.groupLogo.setImageResource(R.drawable.shopping_101)

            "CLOTHING" ->
                binding.groupLogo.setImageResource(R.drawable.shopping_102)

            "BEAUTY" ->
                binding.groupLogo.setImageResource(R.drawable.shopping_103)

            "FOOD" ->
                binding.groupLogo.setImageResource(R.drawable.shopping_104)

            "HEALTH" ->
                binding.groupLogo.setImageResource(R.drawable.shopping_105)

            "RENT" ->
                binding.groupLogo.setImageResource(R.drawable.shopping_106)

            "PETROL_PUMP" ->
                binding.groupLogo.setImageResource(R.drawable.shopping_107)

            "BIKE" ->
                binding.groupLogo.setImageResource(R.drawable.shopping_109)

            "TRANSPORT" ->
                binding.groupLogo.setImageResource(R.drawable.shopping_108)

            "DONATE" ->
                binding.groupLogo.setImageResource(R.drawable.shopping_110)

            "SPORTS" ->
                binding.groupLogo.setImageResource(R.drawable.shopping_111)

            "MOBILE" ->
                binding.groupLogo.setImageResource(R.drawable.shopping_112)

            else ->
                binding.groupLogo.setImageResource(R.drawable.shopping_113)
        }


    }
}