package com.self.expensetracker.splitwise.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.self.expensetracker.splitwise.FirebaseCallback
import com.self.expensetracker.splitwise.MyApplication
import com.self.expensetracker.splitwise.data.CategoryDescriptionData
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.data.RecentTransactionData
import com.self.expensetracker.splitwise.data.SimpleTextData
import com.self.expensetracker.splitwise.databinding.CategoryFragmentLayoutBinding
import com.self.expensetracker.splitwise.ui.CategoryFragmentAdapter
import com.self.expensetracker.splitwise.ui.ExpenseDetailActivity
import com.self.expensetracker.splitwise.ui.util.CategoryCreator
import com.self.expensetracker.splitwise.ui.util.CategoryManager

class CategoryFragment(application: MyApplication, activity: ExpenseDetailActivity) : BaseFragment(application, activity), CategoryCreator {

    lateinit  var recyclerView: RecyclerView
    private lateinit var binding: CategoryFragmentLayoutBinding

    private val list :ArrayList<Data> = ArrayList()

    private val adapter : CategoryFragmentAdapter = CategoryFragmentAdapter(this)

    private val categoryManager : CategoryManager = CategoryManager.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CategoryFragmentLayoutBinding.inflate(layoutInflater)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        setCurrentMonth()
        val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        binding.monthFilter.setOnClickListener {
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(50)
            }
            Toast.makeText(requireContext(), "It is always set to current Month", Toast.LENGTH_SHORT).show()
        }

        prepareData()
        return binding.root
    }

    private var currMonth = -1
    private var currYear = -1

    private fun setCurrentMonth() {
        currMonth = MonthManager.getInstance().getCurrentMonth()
        currYear = MonthManager.getInstance().getCurrentYear()
        binding.userGroupName.text = getMonthName(currMonth) + " " + currYear
    }

    private fun prepareData() {
        list.clear()
        list.add(SimpleTextData("Default monthly budget is set at ₹50000,with ₹10000 allocated for individual categories.\nYou can change it from budget tab"))
        list.add(RecentTransactionData("Expense categories"))
        Log.d("jbkdsasvlh", "total category size  ${categoryManager.getTotalCategoryList().size}")
        categoryManager.getTotalCategoryList().forEach {
            list.add( CategoryDescriptionData(it.categoryName,it.type))
        }
        if (getNewDate(false).month == currMonth && getNewDate(false).year == currYear)
            list.add(SimpleTextData("\nCreate new category feature will be available soon", true))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onResume() {
        super.onResume()
        groupData = getEDetailActivity().getGroupData()
        adapter.setList(list)

    }


    override fun createCategory() {
        openCreateCustomCategoryDialog(object : FirebaseCallback<Boolean> {
            override fun isSuccess(result: Boolean) {
                if (result) {
                    prepareData()
                    adapter.setList(list)
                } else
                    showErrorToast()
            }

            override fun isFailed(reason: String) {
                showErrorToast()
            }
        })
    }

    private fun showErrorToast() {
        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
    }

}