package com.example.splitwise

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.data.Data
import com.example.splitwise.data.DateData
import com.example.splitwise.data.ExpenseCategoryData
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.data.ShoppingData
import com.example.splitwise.databinding.AddExpenseLayoutBinding
import com.example.splitwise.databinding.RecordFragmentLayoutBinding
import com.example.splitwise.ui.CategoryAdapter
import com.example.splitwise.ui.HomeAdapter
import com.example.splitwise.ui.HomeViewModel
import com.example.splitwise.ui.di.component.DaggerExpenseDetailActivityComponent
import com.example.splitwise.ui.di.module.ExpenseDetailActivityModule
import com.example.splitwise.ui.di.module.HomeActivityModule
import com.example.splitwise.ui.util.BEAUTY
import com.example.splitwise.ui.util.CLOTHING
import com.example.splitwise.ui.util.FOOD
import com.example.splitwise.ui.util.HEALTH
import com.example.splitwise.ui.util.MOVIE
import com.example.splitwise.ui.util.PETROL_PUMP
import com.example.splitwise.ui.util.RENT
import com.example.splitwise.ui.util.SHOPPING_GENERAL
import com.example.splitwise.ui.util.UiState
import com.example.splitwise.ui.util.hide
import com.example.splitwise.ui.util.show
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class RecordsFragment(val application: MyApplication,val activity: ExpenseDetailActivity) : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: RecordFragmentLayoutBinding
    private var list: ArrayList<Data> = ArrayList()
    private var groupData: GroupDetailData? = null

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

        binding.searchLayout.fragmentHomeSearchTeamEditTxt.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

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
        abc.hideSoftInputFromWindow(
            binding.searchLayout.fragmentHomeSearchTeamEditTxt.windowToken,
            0
        )

        if (list.isEmpty()) {
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun openCreateGroupDialog() {
        val dialogView = AddExpenseLayoutBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
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
                val data = ShoppingData("",dialogView.shoppingName.text.toString(),filter,dialogView.shoppingPrice.text.toString())
                totalShoppingSum += data.totalAmount.toDouble()
                binding.totalExpense.text = totalShoppingSum.toString()
                binding.totalExpense.setTextColor(requireActivity().resources.getColor(R.color.ce_highlight_khayi_light))
                viewModel.addNewUserExpenses(groupData!!, data, object : FirebaseCallback<Boolean> {
                    override fun isSuccess(result: Boolean) {
                        if (result) {
                            if (isDateChange()) {
                                list.add(0, data)
                                list.add(0, getNewDate())
                            } else {
                                list.add(1, data)
                            }

                            adapter.setList(list)
                        }
                    }

                    override fun isFailed(reason: String) {
                        Log.d("kflwh", "Failed to add expenses $reason")
                    }

                })
                viewModel.updateTotalExpense(groupData!!, totalShoppingSum.toString())
                dialog.dismiss()
            }
        }
        dialog.setContentView(dialogView.root)
        dialog.show()
    }
    private val categoryList: ArrayList<Data> = ArrayList()

    private fun createCategoryList(): ArrayList<Data> {
        categoryList.clear()
        for (i in 101..108) {
            categoryList.add(ExpenseCategoryData(i,false))
        }
        return categoryList

    }

    private fun getFilterType(selectedCategory: Int): String {

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

            else ->
                return "other"
        }

    }

    private fun getNewDate(): DateData {
        val calendar = Calendar.getInstance().apply {
            this.timeInMillis = System.currentTimeMillis()
        }
        var today = getDate(calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH))

        date = calendar.get(Calendar.DATE)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        return DateData(today, date, month, year)

    }

    private fun getDate(date: Int, month: Int): String {
        var today = "$date "
        when (month) {
            Calendar.JANUARY ->
                today += "JANUARY"

            Calendar.FEBRUARY ->
                today += "FEBRUARY"

            Calendar.MARCH ->
                today += "MARCH"

            Calendar.APRIL ->
                today += "APRIL"

            Calendar.MAY ->
                today += "MAY"

            Calendar.JUNE ->
                today += "JUNE"

            Calendar.JULY ->
                today += "JULY"

            Calendar.AUGUST ->
                today += "AUGUST"

            Calendar.SEPTEMBER ->
                today += "SEPTEMBER"

            Calendar.OCTOBER ->
                today += "OCTOBER"

            Calendar.NOVEMBER ->
                today += "NOVEMBER"

            Calendar.DECEMBER ->
                today += "DECEMBER"
        }
        return today
    }

    private fun isDateChange(): Boolean {
        val calendar = Calendar.getInstance().apply {
            this.timeInMillis = System.currentTimeMillis()
        }
        if (calendar.get(Calendar.DATE) != date)
            return true
        if (calendar.get(Calendar.MONTH) != month)
            return true
        if (calendar.get(Calendar.YEAR) != year)
            return true

        return false
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

    override fun onResume() {
        super.onResume()
        totalShoppingSum = 0.0
        if (!isSearchOpen)
            fetchData()
    }

    @SuppressLint("RepeatOnLifecycleWrongUsage")
    private fun fetchData() {
        viewModel.getExpenseDetail(groupData!!)
        groupData?.let {
            binding.userGroupName.text = it.groupName
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.expenseDetail.collect {
                    when (it) {
                        is UiState.Loading -> {
                            list = ArrayList()
                            adapter.setList(list)
                            binding.totalExpense.text = "No Expenses"
                            binding.totalExpense.setTextColor(requireActivity().resources.getColor(R.color.secondary_txt))
                            binding.progressBar.visibility = View.VISIBLE
                            binding.noElement.visibility = View.GONE
                        }

                        is UiState.Success -> {
                            if (isSearchOpen) {
                                return@collect
                            }

                            binding.progressBar.visibility = View.GONE
                            if (it.data.isNotEmpty()) {
                                list = it.data as ArrayList<Data>
                                if (list[0] is DateData) {
                                    date = (list[0] as DateData).currentDate
                                    month = (list[0] as DateData).month
                                    year = (list[0] as DateData).year
                                }
                                adapter.setList(list)
                                totalShoppingSum = 0.0
                                list.forEach { shoppingData ->
                                    if (shoppingData is ShoppingData)
                                        totalShoppingSum += shoppingData.totalAmount.toDouble()
                                }
                                binding.totalExpense.text = totalShoppingSum.toString()
                                binding.totalExpense.setTextColor(requireActivity().resources.getColor(R.color.ce_highlight_khayi_light))
                                binding.noElement.visibility = View.GONE
                            } else {
                                binding.noElement.visibility = View.VISIBLE
                            }

                            viewModel.updateTotalExpense(groupData!!, totalShoppingSum.toString())

                        }

                        else -> {
                            binding.progressBar.visibility = View.GONE
                            binding.noElement.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

}