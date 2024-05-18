package com.example.splitwise.ui.holder

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.CategoryFilterListener
import com.example.splitwise.R
import com.example.splitwise.UpdateRecordsListener
import com.example.splitwise.data.Data
import com.example.splitwise.data.ExpenseCategoryData
import com.example.splitwise.data.ShoppingData
import com.example.splitwise.databinding.AddExpenseLayoutBinding
import com.example.splitwise.databinding.SpendDetailLayoutBinding
import com.example.splitwise.ui.CategoryAdapter
import com.example.splitwise.ui.util.BEAUTY
import com.example.splitwise.ui.util.BIKE
import com.example.splitwise.ui.util.CLOTHING
import com.example.splitwise.ui.util.DONATE
import com.example.splitwise.ui.util.FOOD
import com.example.splitwise.ui.util.HEALTH
import com.example.splitwise.ui.util.MOBILE
import com.example.splitwise.ui.util.MOVIE
import com.example.splitwise.ui.util.PETROL_PUMP
import com.example.splitwise.ui.util.RENT
import com.example.splitwise.ui.util.SHOPPING_GENERAL
import com.example.splitwise.ui.util.SPORTS
import com.example.splitwise.ui.util.TRANSPORT
import com.example.splitwise.ui.util.hide
import com.example.splitwise.ui.util.show
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShoppingDetailHolder(val binding: SpendDetailLayoutBinding,val listener: UpdateRecordsListener,val context: Context) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var categoryAdapter: CategoryAdapter
    val categoryList: ArrayList<Data> = ArrayList()

    private fun createCategoryList(): ArrayList<Data> {
        categoryList.clear()
        for (i in 101..113) {
            categoryList.add(i-101,ExpenseCategoryData(i, false))
        }
        return categoryList

    }

    fun setData(data: Data, isLast: Boolean) {
        val newData = data as ShoppingData
        binding.spendingType.text = data.shoppingName
        binding.totalSum.text = "\u20B9" + data.totalAmount
        binding.spendingDate.text = "${data.date}-${data.month}-${data.year}"
        if (isLast)
            binding.seprator.hide()
        else
            binding.seprator.show()


        binding.root.setOnClickListener {
            openEditRecordsDialog(newData)
        }
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

    private fun openEditRecordsDialog(newData: ShoppingData) {
        createCategoryList()
        val dialogView = AddExpenseLayoutBinding.inflate(LayoutInflater.from(context))
        val dialog = BottomSheetDialog(context,R.style.BottomSheetDialogStyle)
        dialog.behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels

        var selectedCategory = getFilterType(newData.shoppingCategory)
        var previousPosition = selectedCategory - 101
        (categoryList[previousPosition] as ExpenseCategoryData).isSelected = true
        categoryAdapter = CategoryAdapter(object : CategoryFilterListener<Int> {
            override fun selectedFilter(categoryType: Int, position: Int) {
                selectedCategory = categoryType
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

        dialogView.expenseFilterRecylerView.smoothScrollToPosition(previousPosition)

        val previousBudget = newData.totalAmount.toDouble()

        dialogView.updateButton.setOnClickListener {
            if (verifyInput(dialogView)) {
                newData.shoppingName = dialogView.shoppingName.text.toString()
                newData.totalAmount = dialogView.shoppingPrice.text.toString()
                newData.comment = dialogView.descriptionText.text.toString()
                newData.shoppingCategory = getFilterTypeString(selectedCategory)
                listener.updateRecord(newData, newData.totalAmount.toDouble()- previousBudget,position)
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

    fun getFilterTypeString(selectedCategory: Int): String {

        when (selectedCategory) {
            MOVIE ->
                return "MOVIE"

            CLOTHING ->
                return "CLOTHING"

            BEAUTY ->
                return "BEAUTY"

            FOOD ->
                return "FOOD"

            HEALTH ->
                return "HEALTH"

            RENT ->
                return "RENT"

            PETROL_PUMP ->
                return "PETROL_PUMP"

            BIKE ->
                return "BIKE"

            TRANSPORT ->
                return "TRANSPORT"

            DONATE ->
                return "DONATE"

            SPORTS ->
                return "SPORTS"

            MOBILE ->
                return "MOBILE"

            else ->
                return "OTHER"
        }

    }
    private fun getFilterType(selectedCategory: String): Int {

        when (selectedCategory) {
            "MOVIE" ->
                return MOVIE

            "CLOTHING" ->
                return CLOTHING

            "BEAUTY" ->
                return BEAUTY

            "FOOD" ->
                return FOOD

            "HEALTH" ->
                return HEALTH

            "RENT" ->
                return RENT

            "PETROL_PUMP" ->
                return PETROL_PUMP

            "BIKE" ->
                return BIKE

            "TRANSPORT" ->
                return TRANSPORT

            "DONATE" ->
                return DONATE

            "SPORTS" ->
                return SPORTS

            "MOBILE" ->
                return MOBILE

            else ->
                return SHOPPING_GENERAL
        }

    }
}