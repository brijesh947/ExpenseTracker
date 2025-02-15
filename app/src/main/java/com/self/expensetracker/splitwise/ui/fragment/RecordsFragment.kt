package com.self.expensetracker.splitwise.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.Resources
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
import com.self.expensetracker.splitwise.CategoryFilterListener
import com.self.expensetracker.splitwise.FirebaseCallback
import com.self.expensetracker.splitwise.MonthYearPickerDialog
import com.self.expensetracker.splitwise.MyApplication
import com.self.expensetracker.splitwise.R
import com.self.expensetracker.splitwise.UpdateRecordsListener
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.data.ExpenseCategoryData
import com.self.expensetracker.splitwise.data.MonthWiseProgressData
import com.self.expensetracker.splitwise.data.RecentTransactionData
import com.self.expensetracker.splitwise.data.ShoppingData
import com.self.expensetracker.splitwise.databinding.AddExpenseLayoutBinding
import com.self.expensetracker.splitwise.databinding.RecordFragmentLayoutBinding
import com.self.expensetracker.splitwise.ui.CategoryAdapter
import com.self.expensetracker.splitwise.ui.ExpenseDetailActivity
import com.self.expensetracker.splitwise.ui.HomeAdapter
import com.self.expensetracker.splitwise.ui.di.component.DaggerExpenseDetailActivityComponent
import com.self.expensetracker.splitwise.ui.di.module.ExpenseDetailActivityModule
import com.self.expensetracker.splitwise.ui.di.module.HomeActivityModule
import com.self.expensetracker.splitwise.ui.util.SHOPPING_GENERAL
import com.self.expensetracker.splitwise.ui.util.UiState
import com.self.expensetracker.splitwise.ui.util.hide
import com.self.expensetracker.splitwise.ui.util.show
import com.self.expensetracker.splitwise.ui.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


class RecordsFragment(override val application: MyApplication, override val activity: ExpenseDetailActivity) : BaseFragment(application,activity), UpdateRecordsListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: RecordFragmentLayoutBinding
    private var list: ArrayList<Data> = ArrayList()

    var isSearchOpen = false

    private lateinit var categoryAdapter: CategoryAdapter

    @Inject
    lateinit var adapter: HomeAdapter

    @Inject
    lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        initFragment()
        super.onCreate(savedInstanceState)
    }

    private fun fetchCategoryData() {
        viewModel.getCategoryDetail(groupData!!)
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
        setAddRecordButtonVisibilty()
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
        setCurrentMonth()
        recyclerView = binding.expenseRecylerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        adapter.setListener(this)
        return binding.root
    }

    private fun setCurrentMonth() {
        currMonth = MonthManager.getInstance().getCurrentMonth()
        currYear = MonthManager.getInstance().getCurrentYear()
        binding.userGroupName.text = getMonthName(currMonth) + " " + currYear
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        groupData = getEDetailActivity().getGroupData()
        fetchCategoryData();
    }

    private fun openCreateGroupDialog() {
        val dialogView = AddExpenseLayoutBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialogStyle)
        dialog.behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels

        var selectedCategory = SHOPPING_GENERAL
        var selectedCategoryName = "Other"
        var previousPosition = -1
        categoryAdapter = CategoryAdapter(object : CategoryFilterListener<Int> {
            override fun selectedFilter(categoryName: String, categoryType: Int, position: Int) {
                selectedCategory = categoryType
                selectedCategoryName = categoryName
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

        dialogView.updateAddDeleteParent.hide()
        dialogView.createGroupButton.show()
        dialogView.close.setOnClickListener {
            dialog.dismiss()
        }
        dialogView.createGroupButton.setOnClickListener {
            if (verifyInput(dialogView)) {
                binding.noElement.visibility = View.GONE

                val data = ShoppingData(
                    "",
                    dialogView.shoppingName.text.toString(),
                    "" + selectedCategory,
                    selectedCategoryName,
                    dialogView.shoppingPrice.text.toString(),
                    getNewDate(false).month,
                    getNewDate(false).year,
                    getNewDate(false).date,
                    dialogView.descriptionText.text.toString()
                )
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

        setAddRecordButtonVisibilty()


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

    private fun setAddRecordButtonVisibilty() {
        if (currMonth != getNewDate(false).month || currYear != getNewDate(false).year)
            binding.addRecordsButton.hide()
        else
            binding.addRecordsButton.show()
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

            MonthManager.getInstance().setMonthAndYear(currMonth,currYear)
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

    override fun updateRecord(data: ShoppingData, diff: Double, position: Int) {
        Log.d("8iuhgvghjk", "${data.toString()} and position is $position")

        viewModel.updateUserExpenses(groupData!!, data, object : FirebaseCallback<Boolean> {

            override fun isSuccess(result: Boolean) {
                Log.d("8iuhgvghjk", "update isSuccess:")
                adapter.notifyItemChanged(position)
                totalShoppingSum += diff
                (list[0] as MonthWiseProgressData).totalExpense = totalShoppingSum.toLong()
                adapter.notifyItemChanged(0)

            }

            override fun isFailed(reason: String) {
                Log.d("8iuhgvghjk", "update is Failed $reason")
            }

        })

    }

    override fun deleteRecord(data: ShoppingData, position: Int) {
       viewModel.deleteUserExpenses(groupData!!,data,object :FirebaseCallback<Boolean>{
           override fun isSuccess(result: Boolean) {
               Log.d("8iuhgvghjk", "update isSuccess:")
               list.removeAt(position)
               adapter.notifyItemRemoved(position)
               totalShoppingSum -= data.totalAmount.toDouble()
               (list[0] as MonthWiseProgressData).totalExpense = totalShoppingSum.toLong()
               adapter.notifyItemChanged(0)
           }

           override fun isFailed(reason: String) {
               Log.d("8iuhgvghjk", "update is Failed $reason")
           }

       })
    }

}