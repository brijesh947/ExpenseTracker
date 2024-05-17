package com.example.splitwise.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.CategoryFilterListener
import com.example.splitwise.FirebaseCallback
import com.example.splitwise.MonthYearPickerDialog
import com.example.splitwise.MyApplication
import com.example.splitwise.R
import com.example.splitwise.data.Data
import com.example.splitwise.data.DateData
import com.example.splitwise.data.ExpenseCategoryData
import com.example.splitwise.data.ExpenseFilterData
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.data.MonthWiseProgressData
import com.example.splitwise.data.RecentTransactionData
import com.example.splitwise.data.ShoppingData
import com.example.splitwise.databinding.AddExpenseLayoutBinding
import com.example.splitwise.databinding.RecordFragmentLayoutBinding
import com.example.splitwise.ui.CategoryAdapter
import com.example.splitwise.ui.ExpenseDetailActivity
import com.example.splitwise.ui.ExpenseFilterListener
import com.example.splitwise.ui.HomeAdapter
import com.example.splitwise.ui.di.component.DaggerExpenseDetailActivityComponent
import com.example.splitwise.ui.di.module.ExpenseDetailActivityModule
import com.example.splitwise.ui.di.module.HomeActivityModule
import com.example.splitwise.ui.util.CURR_MONTH_FILTER
import com.example.splitwise.ui.util.NO_FILTER
import com.example.splitwise.ui.util.PREV_MONTH_FILTER
import com.example.splitwise.ui.util.SHOPPING_GENERAL
import com.example.splitwise.ui.util.UiState
import com.example.splitwise.ui.util.hide
import com.example.splitwise.ui.util.show
import com.example.splitwise.ui.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


class RecordsFragment(val application: MyApplication, val activity: ExpenseDetailActivity) : BaseFragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: RecordFragmentLayoutBinding
    private var list: ArrayList<Data> = ArrayList()
    private var groupData: GroupDetailData? = null
    private var currExpesneFilter: Int = NO_FILTER

    var isSearchOpen = false
    private var date = -1
    private var month = -1
    private var year = -1

    private lateinit var categoryAdapter: CategoryAdapter

    @Inject
    lateinit var adapter: HomeAdapter

    @Inject
    lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        initFragment()
        super.onCreate(savedInstanceState)

    }

    private val searchList: ArrayList<ShoppingData> = ArrayList()


    private fun openSearchPanel() {
        isSearchOpen = true
        binding.toolbarParent.hide()
        binding.progressBar.hide()
        binding.expenseRecylerview.hide()
        binding.addRecordsButton.hide()
        binding.noElement.hide()
        adapter.setList(ArrayList())
        binding.searchLayout.root.show()
        binding.searchLayout.fragmentHomeSearchTeamEditTxt.requestFocus()
        val imm = application.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(
            binding.searchLayout.fragmentHomeSearchTeamEditTxt,
            InputMethodManager.SHOW_IMPLICIT
        )


        binding.searchLayout.fragmentHomeSearchRecyclerview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchLayout.fragmentHomeSearchRecyclerview.adapter = adapter


        binding.searchLayout.fragmentHomeSearchClearTextButton.setOnClickListener {
            binding.searchLayout.fragmentHomeSearchTeamEditTxt.setText("")
            adapter.setList(ArrayList())
        }

        binding.searchLayout.fragmentHomeSearchTeamEditTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null)
                    return
                searchList.clear()
                val searchText = s.toString().trim().toLowerCase()

                if (s.isNotEmpty()) {
                    binding.searchLayout.fragmentHomeSearchClearTextButton.show()
                    list.forEach {
                        if (it is ShoppingData)
                            if (it.shoppingName.trim().toLowerCase().contains(searchText)) {
                                searchList.add(it)
                            }
                    }
                } else {
                    binding.searchLayout.fragmentHomeSearchClearTextButton.hide()
                    adapter.setList(ArrayList())
                }
                if (searchList.isNotEmpty()) {
                    binding.searchLayout.fragmentHomeSearchRecyclerview.show()
                    binding.searchLayout.emptyStateText.hide()
                    adapter.setList(searchList)
                } else {
                    adapter.setList(ArrayList())
                    binding.searchLayout.fragmentHomeSearchRecyclerview.hide()
                    binding.searchLayout.emptyStateText.show()
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.searchLayout.fragmentHomeSearchCancelBtn.setOnClickListener {
            closeSearchPanel()
        }
    }

    private fun closeSearchPanel() {
        isSearchOpen = false
        binding.searchLayout.root.hide()
        binding.toolbarParent.show()
        binding.expenseRecylerview.show()
        binding.addRecordsButton.show()
        binding.searchLayout.fragmentHomeSearchTeamEditTxt.setText("")
        binding.searchLayout.fragmentHomeSearchTeamEditTxt.clearFocus()

        val abc = application.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        abc.hideSoftInputFromWindow(binding.searchLayout.fragmentHomeSearchTeamEditTxt.windowToken, 0)

        if (list.isEmpty()) {
            binding.noElement.text = "Add Expenses to show here."
            binding.noElement.show()
        } else {
            adapter.setList(list)
        }

    }

    fun setGroupData(data: GroupDetailData?) {
        groupData = data
    }

    private fun initFragment() {
        DaggerExpenseDetailActivityComponent.builder()
            .applicationComponent((application as MyApplication).applicationComponent)
            .homeActivityModule(HomeActivityModule(activity = activity as AppCompatActivity))
            .expenseDetailActivityModule(ExpenseDetailActivityModule(application, activity))
            .build()
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = RecordFragmentLayoutBinding.inflate(layoutInflater)
        binding.addRecordsButton.setOnClickListener {
            openCreateGroupDialog()
        }

        binding.searchButton.setOnClickListener {
            openSearchPanel()
        }

        recyclerView = binding.expenseRecylerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
        }
        currMonth = calendar.get(Calendar.MONTH)
        currYear = calendar.get(Calendar.YEAR)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun openCreateGroupDialog() {
        val dialogView = AddExpenseLayoutBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialogStyle)
        var selectedCategory = SHOPPING_GENERAL
        var previousPosition = -1
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
        dialogView.expenseFilterRecylerView.layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.HORIZONTAL,false)
        dialogView.expenseFilterRecylerView.adapter = categoryAdapter
        categoryAdapter.setList(createCategoryList())
        dialogView.createGroupButton.setOnClickListener {
            if (verifyInput(dialogView)) {
                binding.noElement.visibility = View.GONE
                var filter = getFilterType(selectedCategory)
                val data = ShoppingData("",dialogView.shoppingName.text.toString(),filter,dialogView.shoppingPrice.text.toString(),getNewDate(false).month,getNewDate(false).year,getNewDate(false).date)
                totalShoppingSum += data.totalAmount.toDouble()
                viewModel.addNewUserExpenses(groupData!!, data, object : FirebaseCallback<Boolean> {
                    override fun isSuccess(result: Boolean) {
                        if (result) {
                            if (list.isEmpty()) {
                                list.add(0, data)
                                list.add(0, RecentTransactionData("Recent Transaction"))
                                list.add(0, MonthWiseProgressData(50000, data.totalAmount.toLong()))

                            } else {
                                list.add(2, data)
                            }

                            if (list[0] is MonthWiseProgressData)
                                (list[0] as MonthWiseProgressData).totalExpense = totalShoppingSum.toLong()
                            adapter.setList(list)
                            updateCurrentMonthExpenseForHome()
                        }
                    }

                    override fun isFailed(reason: String) {
                        Log.d("kflwh", "Failed to add expenses $reason")
                    }

                })
                dialog.dismiss()
            }
        }

        dialog.setContentView(dialogView.root)
        dialog.show()
    }

    private fun updateCurrentMonthExpenseForHome() {
        viewModel.updateTotalExpense(groupData!!, totalShoppingSum.toString())
    }

    private fun getNewDate(needToUpdate: Boolean): DateData {
        val calendar = Calendar.getInstance().apply {
            this.timeInMillis = System.currentTimeMillis()
        }
        var today = getDate(calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH))
        if (needToUpdate) {
            date = calendar.get(Calendar.DATE)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
        }
        return DateData(
            today,
            calendar.get(Calendar.DATE),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.YEAR)
        )

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

    private var totalShoppingSum = 0.0

    private var currMonth: Int = -1
    private var currYear: Int = -1

    override fun onResume() {
        super.onResume()
        totalShoppingSum = 0.0

        binding.monthFilter.setOnClickListener {
            openMonthSelectorDialog()
        }

        if (!isSearchOpen)
            fetchData(currMonth,currYear)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private val HIDE_THRESHOLD = 20
            private var scrolledDistance = 0
            private var controlsVisible = true
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    // Hide the floating button with animation
                    binding.addRecordsButton.animate().translationY((binding.addRecordsButton.height + 110).toFloat())
                        .setInterpolator(AccelerateInterpolator())
                        .setDuration(300).start()
                    controlsVisible = false
                    scrolledDistance = 0
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    // Show the floating button with animation
                    binding.addRecordsButton.animate().translationY(0F)
                        .setInterpolator(DecelerateInterpolator())
                        .setDuration(300).start()
                    controlsVisible = true
                    scrolledDistance = 0
                }
                if (controlsVisible && dy > 0 || !controlsVisible && dy < 0) {
                    scrolledDistance += dy
                }
            }
        })

    }

    private fun openMonthSelectorDialog() {
        var calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
        }
        val pd = MonthYearPickerDialog.newInstance(
            currMonth+1,
            calendar[Calendar.DAY_OF_MONTH], currYear
        )

        pd.setListener { view, selectedYear, selectedMonth, selectedDay ->
            currMonth = selectedMonth - 1
            currYear = selectedYear
            if (currMonth != calendar.get(Calendar.MONTH) || currYear != calendar.get(Calendar.YEAR)) {
                binding.addRecordsButton.hide()
            } else {
                binding.addRecordsButton.show()
                binding.addRecordsButton.animate().translationY(0F)
                    .setInterpolator(DecelerateInterpolator())
                    .setDuration(300).start()
            }

            fetchData(currMonth, currYear)
            binding.userGroupName.text = getMonthName(currMonth) + " " + currYear
        }
        pd.show(requireFragmentManager(), "MonthYearPickerDialog")
    }

    @SuppressLint("RepeatOnLifecycleWrongUsage")
    private fun fetchData(currMonth: Int, currYear: Int) {
        viewModel.getExpenseDetail(groupData!!, currMonth, currYear)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.expenseDetail.collect {
                    when (it) {
                        is UiState.Loading -> {
                            list = ArrayList()
                            adapter.setList(list)
                            binding.progressBar.show()
                            binding.noElement.hide()
                        }

                        is UiState.Success -> {
                            if (isSearchOpen) {
                                return@collect
                            }

                            binding.progressBar.visibility = View.GONE
                            if (it.data.isNotEmpty()) {
                                list = it.data as ArrayList<Data>
                                if (list[0] is MonthWiseProgressData) {
                                    totalShoppingSum = (list[0] as MonthWiseProgressData).totalExpense.toDouble()
                                    (list[0] as MonthWiseProgressData).totalBudget = requireContext().getSharedPreferences("budget", Context.MODE_PRIVATE).getLong("" + currMonth + "_" + currYear, 50000)
                                }

                                adapter.setList(list)
                                binding.noElement.hide()
                            } else {
                                binding.noElement.text =
                                    if (binding.addRecordsButton.visibility == View.VISIBLE)
                                        "Add Expenses to show here."
                                    else
                                        "No Expenses to show here"
                                binding.noElement.show()
                            }

                           updateCurrentMonthExpenseForHome()
                        }

                        else -> {
                            binding.progressBar.hide()
                            binding.noElement.text = "No Expenses to show here."
                            binding.noElement.show()
                        }
                    }
                }
            }
        }
    }

}