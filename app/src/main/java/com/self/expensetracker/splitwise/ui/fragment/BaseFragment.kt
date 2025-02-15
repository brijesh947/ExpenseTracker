package com.self.expensetracker.splitwise.ui.fragment

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.self.expensetracker.splitwise.CategoryFilterListener
import com.self.expensetracker.splitwise.FirebaseCallback
import com.self.expensetracker.splitwise.MyApplication
import com.self.expensetracker.splitwise.R
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.data.DateData
import com.self.expensetracker.splitwise.data.ExpenseCategoryData
import com.self.expensetracker.splitwise.data.GroupDetailData
import com.self.expensetracker.splitwise.databinding.CreateCustomCategoryLayoutBinding
import com.self.expensetracker.splitwise.ui.CategoryAdapter
import com.self.expensetracker.splitwise.ui.ExpenseDetailActivity
import com.self.expensetracker.splitwise.ui.di.component.DaggerExpenseDetailActivityComponent
import com.self.expensetracker.splitwise.ui.di.module.ExpenseDetailActivityModule
import com.self.expensetracker.splitwise.ui.di.module.HomeActivityModule
import com.self.expensetracker.splitwise.ui.util.CategoryManager

import com.self.expensetracker.splitwise.ui.util.SHOPPING_GENERAL
import com.self.expensetracker.splitwise.ui.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Calendar
import javax.inject.Inject

open class BaseFragment(open val application: MyApplication, open val activity: ExpenseDetailActivity) : Fragment() {


    val categoryList: ArrayList<Data> = ArrayList()
    private val categoryManager : CategoryManager = CategoryManager.getInstance()
    private val customCategoryList = ArrayList<Data>()

    private var mActivity:ExpenseDetailActivity? = null


    fun getEDetailActivity(): ExpenseDetailActivity {
        if (mActivity == null) {
            if (getActivity() == null) onAttach(activity)
            mActivity = getActivity() as ExpenseDetailActivity
        }
        return mActivity!!
    }


    @Inject
    lateinit var viewModelRF: HomeViewModel

    var groupData: GroupDetailData? = null




    private lateinit var  categoryAdapter : CategoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        initFragmentBase(application, activity)
        super.onCreate(savedInstanceState)
    }

    private fun initFragmentBase(application: MyApplication, activity: ExpenseDetailActivity) {
        DaggerExpenseDetailActivityComponent.builder()
            .applicationComponent((application as MyApplication).applicationComponent)
            .homeActivityModule(HomeActivityModule(activity = activity as AppCompatActivity))
            .expenseDetailActivityModule(ExpenseDetailActivityModule(application, activity))
            .build()
            .inject(this)
    }

    fun getNewDate(needToUpdate: Boolean): DateData {
        val calendar = Calendar.getInstance().apply {
            this.timeInMillis = System.currentTimeMillis()
        }
        var today = getDate(calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH))
        return DateData(
            today,
            calendar.get(Calendar.DATE),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.YEAR)
        )
    }


    fun createCategoryList(): ArrayList<Data> {
        categoryList.clear()
        var i = 0
        categoryManager.getTotalCategoryList().forEach {
            categoryList.add(i++, ExpenseCategoryData(it.categoryName, it.type, false))
        }
        return categoryList
    }


    fun getDate(date: Int, month: Int): String {
        var today = "$date"
        //today += getMonthName(month)
        return today
    }

    fun getMonthName(month: Int): String {
        when (month) {
            Calendar.JANUARY ->
                return "Jan"

            Calendar.FEBRUARY ->
                return "Feb"

            Calendar.MARCH ->
                return "Mar"

            Calendar.APRIL ->
                return "Apr"

            Calendar.MAY ->
                return "May"

            Calendar.JUNE ->
                return "Jun"

            Calendar.JULY ->
                return "Jul"

            Calendar.AUGUST ->
                return "Aug"

            Calendar.SEPTEMBER ->
                return "Sep"

            Calendar.OCTOBER ->
                return "Oct"

            Calendar.NOVEMBER ->
                return "Nov"

            Calendar.DECEMBER ->
                return "Dec"
        }
        return ""
    }

    fun openCreateCustomCategoryDialog(callback: FirebaseCallback<Boolean>) {
        val dialogView = CreateCustomCategoryLayoutBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        dialog.behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels

        var selectedCategory = SHOPPING_GENERAL
        var selectedCategoryName = "Other"
        var previousPosition = -1
        categoryAdapter = CategoryAdapter(object : CategoryFilterListener<Int> {
            override fun selectedFilter(categoryName: String, categoryType: Int, position: Int) {
                selectedCategory = categoryType
                selectedCategoryName = categoryName
                (customCategoryList[position] as ExpenseCategoryData).isSelected = true
                if (previousPosition != -1 && previousPosition != position) {
                    (customCategoryList[previousPosition] as ExpenseCategoryData).isSelected = false
                    categoryAdapter.notifyItemChanged(previousPosition)
                }
                previousPosition = position
                categoryAdapter.notifyItemChanged(position)
            }
        })
        dialogView.expenseFilterRecylerView.layoutManager = GridLayoutManager(requireContext(),2,
            GridLayoutManager.HORIZONTAL,false)
        dialogView.expenseFilterRecylerView.adapter = categoryAdapter

        categoryAdapter.setList(createMultipleCategoryList())
        dialogView.close.setOnClickListener {
            dialog.dismiss()
        }

        dialogView.updateButton.setOnClickListener {
            Log.d("sdfndnfvew", "is group data null ${groupData == null} and category name is $selectedCategoryName and category type is $selectedCategory")
            groupData?.let {
                if (verifyInput(dialogView.customCategoryName)) {
                    viewModelRF.createNewCategory(
                        groupData!!,
                        CategoryManager.CategoryHolderData(
                            selectedCategory,
                            dialogView.customCategoryName.text.toString(),
                            "",
                            0
                        ), callback
                    )
                    dialog.dismiss()
                }
            }


        }

        dialog.setContentView(dialogView.root)
        dialog.show()
    }

    private fun verifyInput(inputName: EditText): Boolean {
        if (inputName.text.toString().isEmpty()) {
            inputName.error = "Please enter category name"
            return false
        }
        return true
    }

    private fun createMultipleCategoryList(): ArrayList<Data> {
       customCategoryList.clear()
        for (i in 140 downTo 101) {
            customCategoryList.add(ExpenseCategoryData("", i, false,true))
        }
        return customCategoryList
    }


}