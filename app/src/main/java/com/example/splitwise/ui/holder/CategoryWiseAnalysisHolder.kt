package com.example.splitwise.ui.holder

import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.splitwise.R
import com.example.splitwise.data.CategoryAnalysisData
import com.example.splitwise.data.Data
import com.example.splitwise.data.TimeWiseCategoryData
import com.example.splitwise.databinding.CategoryWiseRecordsLayoutBinding
import com.example.splitwise.databinding.PercentageWiseCategoryAnalysisBinding
import com.example.splitwise.ui.TimeWiseCategoryAdapter
import com.example.splitwise.ui.util.hide
import com.example.splitwise.ui.util.show
import com.example.splitwise.ui.util.showRupeeString
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Calendar

class CategoryWiseAnalysisHolder(val binding: PercentageWiseCategoryAnalysisBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {


    private var month = -1;
    private var year = -1;
    fun setData(data: Data,isLastItem:Boolean) {

        val categoryData = data as CategoryAnalysisData
        binding.spendingType.text = categoryData.categoryName
        var totalCategoryExpense = 0.0
        for (item in categoryData.totalExpenseInCategory) {
            totalCategoryExpense += item.second
        }
        categoryData.finalExpenseInCategory = totalCategoryExpense

        val totalPercent = (totalCategoryExpense * 100.0) / categoryData.totalExpense
        val formattedTotalPercent = String.format("%.1f", totalPercent)


        binding.totalExpense.text = showRupeeString(totalCategoryExpense.toLong().toString())
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

        binding.root.setOnClickListener {
            openTotalRecordsDialog(categoryData)
        }


        binding.expenseProgress.progressDrawable = progressDrawable
        binding.expenseProgress.setProgress(totalPercent.toInt(),true)

        if (isLastItem)
            binding.seprator.hide()
        else
            binding.seprator.show()
        setImageIcon(categoryData, binding)

    }

    private fun openTotalRecordsDialog(categoryData: CategoryAnalysisData) {
        val dialogView = CategoryWiseRecordsLayoutBinding.inflate(LayoutInflater.from(context))
        val dialog = BottomSheetDialog(context,R.style.BottomSheetDialogStyle)
//        dialog.behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels

        dialogView.categoryName.text = categoryData.categoryName
        dialogView.closeButton.setOnClickListener {
            dialog.dismiss()
        }



        val list: ArrayList<Data> = createData(categoryData)

        setMonthAndYear((list[0] as TimeWiseCategoryData).time)
        val adapter = TimeWiseCategoryAdapter()

        val pref = context.getSharedPreferences("budget",Context.MODE_PRIVATE)

        val budget = pref.getLong("" + month + "_" + year + "_" + categoryData.categoryName,10000)

        dialogView.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        dialogView.recyclerView.adapter = adapter
        adapter.setList(list)
        dialogView.spent.text = showRupeeString(categoryData.finalExpenseInCategory.toInt().toString())
        dialogView.remaining.text = showRupeeString((budget - categoryData.finalExpenseInCategory.toLong()).toString())
        dialogView.limit.text = showRupeeString(budget)
        setImageIcon(categoryData,dialogView)
        dialog.setContentView(dialogView.root)
        dialog.show()

    }

    private fun setMonthAndYear(time: Long) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = time
        }
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
    }

    private fun createData(categoryData: CategoryAnalysisData): ArrayList<Data> {
        val list: ArrayList<Data> = ArrayList()
        for (item in categoryData.totalExpenseInCategory) {
            list.add(TimeWiseCategoryData(item.first, item.second))
        }
        return list
    }

    private fun setImageIcon(categoryData: CategoryAnalysisData, binding: ViewBinding) {

        when (categoryData.categoryName) {
            "MOVIE" -> {
                if (binding is PercentageWiseCategoryAnalysisBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_101)
                else if (binding is CategoryWiseRecordsLayoutBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_101)
            }

            "CLOTHING" -> {
                if (binding is PercentageWiseCategoryAnalysisBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_102)
                else if (binding is CategoryWiseRecordsLayoutBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_102)

            }

            "BEAUTY" -> {
                if (binding is PercentageWiseCategoryAnalysisBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_103)
                else if (binding is CategoryWiseRecordsLayoutBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_103)

            }

            "FOOD" -> {
                if (binding is PercentageWiseCategoryAnalysisBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_104)
                else if (binding is CategoryWiseRecordsLayoutBinding)
                    binding.groupLogo.setImageResource(R.drawable.pizza_food)
            }

            "HEALTH" -> {
                if (binding is PercentageWiseCategoryAnalysisBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_105)
                else if (binding is CategoryWiseRecordsLayoutBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_105)

            }

            "RENT" -> {
                if (binding is PercentageWiseCategoryAnalysisBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_106)
                else if (binding is CategoryWiseRecordsLayoutBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_106)

            }

            "PETROL_PUMP" -> {
                if (binding is PercentageWiseCategoryAnalysisBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_107)
                else if (binding is CategoryWiseRecordsLayoutBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_107)

            }

            "BIKE" -> {
                if (binding is PercentageWiseCategoryAnalysisBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_109)
                else if (binding is CategoryWiseRecordsLayoutBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_109)

            }

            "TRANSPORT" -> {
                if (binding is PercentageWiseCategoryAnalysisBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_108)
                else if (binding is CategoryWiseRecordsLayoutBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_108)

            }

            "DONATE" -> {
                if (binding is PercentageWiseCategoryAnalysisBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_110)
                else if (binding is CategoryWiseRecordsLayoutBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_110)

            }

            "SPORTS" -> {
                if (binding is PercentageWiseCategoryAnalysisBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_111)
                else if (binding is CategoryWiseRecordsLayoutBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_111)

            }

            "MOBILE" -> {
                if (binding is PercentageWiseCategoryAnalysisBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_112)
                else if (binding is CategoryWiseRecordsLayoutBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_112)

            }

            else -> {
                if (binding is PercentageWiseCategoryAnalysisBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_113)
                else if (binding is CategoryWiseRecordsLayoutBinding)
                    binding.groupLogo.setImageResource(R.drawable.shopping_113)

            }
        }


    }

}