package com.self.expensetracker.splitwise.ui.holder

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.self.expensetracker.splitwise.CategoryFilterListener
import com.self.expensetracker.splitwise.R
import com.self.expensetracker.splitwise.UpdateRecordsListener
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.data.ExpenseCategoryData
import com.self.expensetracker.splitwise.data.ShoppingData
import com.self.expensetracker.splitwise.databinding.AddExpenseLayoutBinding
import com.self.expensetracker.splitwise.databinding.SpendDetailLayoutBinding
import com.self.expensetracker.splitwise.ui.CategoryAdapter
import com.self.expensetracker.splitwise.ui.util.CategoryManager
import com.self.expensetracker.splitwise.ui.util.hide
import com.self.expensetracker.splitwise.ui.util.show
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShoppingDetailHolder(val binding: SpendDetailLayoutBinding,val listener: UpdateRecordsListener,val context: Context) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var categoryAdapter: CategoryAdapter
    val categoryList: ArrayList<Data> = ArrayList()
    private val categoryManager :CategoryManager = CategoryManager.getInstance()

    private fun createCategoryList(newData: ShoppingData): ArrayList<Data> {
        categoryList.clear()

        categoryManager.getTotalCategoryList().forEach {
            categoryList.add(ExpenseCategoryData(it.categoryName,it.type, newData.shoppingCategory == "" + it.type))
        }
        return categoryList
    }

    fun setData(data: Data, isLast: Boolean) {
        val newData = data as ShoppingData
        binding.spendingType.text = data.shoppingName
        binding.totalSum.text = "\u20B9" + data.totalAmount
        binding.spendingDate.text = "${data.date}-${data.month +1}-${data.year}"
        if (isLast)
            binding.seprator.hide()
        else
            binding.seprator.show()


        binding.root.setOnClickListener {
            openEditRecordsDialog(newData)
        }

        binding.groupLogo.setImageResource(context.resources.getIdentifier("shopping_${data.shoppingCategory}", "drawable", context.packageName))

    }

    private fun openEditRecordsDialog(newData: ShoppingData) {
        createCategoryList(newData)
        val dialogView = AddExpenseLayoutBinding.inflate(LayoutInflater.from(context))
        val dialog = BottomSheetDialog(context,R.style.BottomSheetDialogStyle)
        dialog.behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels

        var selectedCategory = newData.shoppingCategory
        var selectedCategoryType = newData.shoppingCategoryType

        var previousPosition = -1

        categoryAdapter = CategoryAdapter(object : CategoryFilterListener<Int> {
            override fun selectedFilter(categoryName: String, categoryType: Int, position: Int) {
                selectedCategory = "" + categoryType
                selectedCategoryType = categoryName
                (categoryList[position] as ExpenseCategoryData).isSelected = true
                if (previousPosition != -1 && previousPosition != position) {
                    (categoryList[previousPosition] as ExpenseCategoryData).isSelected = false
                    categoryAdapter.notifyItemChanged(previousPosition)
                }
                previousPosition = position
                categoryAdapter.notifyItemChanged(position)
            }

        })

        dialogView.updateAddDeleteParent.show()
        dialogView.createGroupButton.hide()

        dialogView.shoppingPrice.setText(newData.totalAmount)
        dialogView.shoppingName.setText(newData.shoppingName)
        dialogView.descriptionText.setText(newData.comment)
        dialogView.expenseFilterRecylerView.layoutManager = GridLayoutManager(context,2,
            GridLayoutManager.HORIZONTAL,false)
        dialogView.expenseFilterRecylerView.adapter = categoryAdapter
        categoryAdapter.setList(categoryList)
        previousPosition = categoryAdapter.findPreviousCategoryPosition()

        dialogView.expenseFilterRecylerView.smoothScrollToPosition(previousPosition)

        val previousBudget = newData.totalAmount.toDouble()

        dialogView.updateButton.setOnClickListener {
            if (verifyInput(dialogView)) {
                newData.shoppingName = dialogView.shoppingName.text.toString()
                newData.totalAmount = dialogView.shoppingPrice.text.toString()
                newData.comment = dialogView.descriptionText.text.toString()
                newData.shoppingCategory = "" + selectedCategory
                newData.shoppingCategoryType = selectedCategoryType
                listener.updateRecord(newData, newData.totalAmount.toDouble() - previousBudget,position)
                dialog.dismiss()
            }
        }

        dialogView.close.setOnClickListener {
            dialog.dismiss()
        }

        dialogView.deleteButton.setOnClickListener {
            listener.deleteRecord(newData, position)
            dialog.dismiss()
        }

        dialog.setContentView(dialogView.root)
        dialog.show()

    }

    private fun verifyInput(dialogView: AddExpenseLayoutBinding): Boolean {
        if (dialogView.shoppingName.text.isEmpty()) {
            dialogView.shoppingName.error = "Shopping name can't be Empty"
            return false
        }
        if (dialogView.shoppingPrice.text.isEmpty()) {
            dialogView.shoppingPrice.error = "Shopping price can't be Empty"
            return false
        }

        return true
    }

}