package com.example.splitwise.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.MyApplication
import com.example.splitwise.R
import com.example.splitwise.data.CategoryAnalysisData
import com.example.splitwise.data.Data
import com.example.splitwise.data.PieChartData
import com.example.splitwise.databinding.AnalysisFilterSelectorBinding
import com.example.splitwise.databinding.AnalysisFragmentLayoutBinding
import com.example.splitwise.ui.AnalysisAdapter
import com.example.splitwise.ui.ExpenseDetailActivity
import com.example.splitwise.ui.di.component.DaggerExpenseDetailActivityComponent
import com.example.splitwise.ui.di.module.ExpenseDetailActivityModule
import com.example.splitwise.ui.di.module.HomeActivityModule
import com.example.splitwise.ui.util.UiState
import com.example.splitwise.ui.util.hide
import com.example.splitwise.ui.util.show
import com.example.splitwise.ui.viewmodel.AnalysisViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Collections
import javax.inject.Inject

class AnalysisFragment(val application: MyApplication, val activity: ExpenseDetailActivity) : BaseFragment(),Comparator<Data> {
    private lateinit var binding: AnalysisFragmentLayoutBinding

    private var currMonth = -1
    private var currYear = -1

    private val BACKWORD = 10
    private val FORWORD = 11

    private val NO_FILTER = 101
    private val ASCENDING_ORDER = 102
    private val DESCENDING_ORDER = 103

    private var currFilter = NO_FILTER

    @Inject
    lateinit var viewModel: AnalysisViewModel

    @Inject
    lateinit var adapter: AnalysisAdapter

    private lateinit var recyclerView: RecyclerView

    private var list = ArrayList<Data>()


    override fun onCreate(savedInstanceState: Bundle?) {
        initFragment()
        super.onCreate(savedInstanceState)
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
        binding = AnalysisFragmentLayoutBinding.inflate(layoutInflater)
        setCurrentMonth()
        binding.monthNameFilter.text = getCurrentMonth(currMonth,currYear)
        binding.backButton.setOnClickListener {
            getDirectionWiseMonth(BACKWORD)
        }
        binding.nextButton.setOnClickListener {
           getDirectionWiseMonth(FORWORD)
        }

        recyclerView = binding.expenseRecylerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        binding.userGroupName.text = activity.getGroupData().groupName
        binding.filterButton.setOnClickListener {
            val manageExpenseFilterDialog = BottomSheetDialog(activity, R.style.BottomSheetDialogStyle)
            val manageNotificationsDialogBinding =
                AnalysisFilterSelectorBinding.inflate(LayoutInflater.from(activity))
            manageExpenseFilterDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            manageExpenseFilterDialog.behavior.skipCollapsed = true
            manageExpenseFilterDialog.setContentView(manageNotificationsDialogBinding.root)
            manageNotificationsDialogBinding.doneButton.setOnClickListener { v -> manageExpenseFilterDialog.dismiss() }

            var prevFilter = currFilter
            showCheckStatus(prevFilter, manageNotificationsDialogBinding)

            manageNotificationsDialogBinding.ascendingOrder.setOnClickListener {
                currFilter = ASCENDING_ORDER
                showCheckStatus(currFilter, manageNotificationsDialogBinding)
            }
            manageNotificationsDialogBinding.noFilter.setOnClickListener {
                currFilter = NO_FILTER
                showCheckStatus(currFilter, manageNotificationsDialogBinding)
            }
            manageNotificationsDialogBinding.descendingOrder.setOnClickListener {
                currFilter = DESCENDING_ORDER
                showCheckStatus(currFilter, manageNotificationsDialogBinding)

            }

            manageExpenseFilterDialog.setOnDismissListener {
                Log.d("sdihfgu", "dismis listener is called")
                if (prevFilter != currFilter)
                    sortRecylerViewData(currFilter)
            }


            if (!manageExpenseFilterDialog.isShowing)
                manageExpenseFilterDialog.show()
        }
        return binding.root
    }

    private fun sortRecylerViewData(currFilter: Int) {

        val newList = list.subList(1, list.size)
        Collections.sort(newList, this)
        val finalList: ArrayList<Data> = ArrayList()
        finalList.add(list[0]);
        finalList.addAll(newList)
        adapter.setList(finalList)

    }

    private fun showCheckStatus(currFilter: Int, filterBinding: AnalysisFilterSelectorBinding) {
        when (currFilter) {
            NO_FILTER -> {
                filterBinding.noCheckbox.isChecked = true
                filterBinding.ascendingCheckbox.isChecked = false
                filterBinding.descendingCheckbox.isChecked = false
            }

            ASCENDING_ORDER -> {
                filterBinding.noCheckbox.isChecked = false
                filterBinding.ascendingCheckbox.isChecked = true
                filterBinding.descendingCheckbox.isChecked = false
            }

            DESCENDING_ORDER -> {
                filterBinding.noCheckbox.isChecked = false
                filterBinding.ascendingCheckbox.isChecked = false
                filterBinding.descendingCheckbox.isChecked = true
            }
        }

    }

    private fun getDirectionWiseMonth(direction: Int) {
        when (direction) {
            BACKWORD -> {
                val prevMonth = getPreviousMonth(currMonth)
                val prevYear = getPrevYear(currMonth,currYear)
                currMonth = prevMonth
                currYear = prevYear
                binding.monthNameFilter.text = getCurrentMonth(currMonth, currYear)
                fetchData()
            }

            FORWORD -> {
                val nextMonth = getNextMonth(currMonth)
                val nextYear = getNextYear(currMonth,currYear)
                currMonth = nextMonth
                currYear = nextYear
                binding.monthNameFilter.text = getCurrentMonth(currMonth, currYear)
                fetchData()
            }
        }
    }
    private fun setCurrentMonth() {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis();
        }
        currMonth = calendar.get(Calendar.MONTH)
        currYear = calendar.get(Calendar.YEAR)

    }




    @SuppressLint("RepeatOnLifecycleWrongUsage")
    fun fetchData() {
        viewModel.getMonthWiseData(activity.getGroupData(), currMonth, currYear)

        try {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.expenseDetail.collect {
                        when (it) {
                            is UiState.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                                binding.noElement.visibility = View.GONE
                                binding.totalSpending.text = "NA"
                                recyclerView.hide()
                            }

                            is UiState.Success -> {
                                binding.progressBar.visibility = View.GONE
                                if (it.data.isNotEmpty()) {
                                    if (checkDataIsPresent(it.data)) {
                                        recyclerView.show()
                                        binding.noElement.visibility = View.GONE
                                        adapter.setList(it.data as ArrayList<Data>)
                                        binding.totalSpending.text =
                                            (it.data[0] as PieChartData).total
                                        list = it.data
                                    } else {
                                        binding.noElement.visibility = View.VISIBLE
                                        binding.totalSpending.text = "NA"
                                        recyclerView.hide()
                                    }
                                } else {
                                    binding.noElement.visibility = View.VISIBLE
                                    recyclerView.hide()
                                    binding.totalSpending.text = "NA"
                                }
                            }

                            else -> {
                                binding.progressBar.visibility = View.GONE
                                binding.noElement.visibility = View.VISIBLE
                                recyclerView.hide()
                                binding.totalSpending.text = "NA"
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("sdfvfr", "setData: ${e.message}")
        }

    }

    private fun checkDataIsPresent(data: List<Data>): Boolean {
        if (data.isEmpty())
            return false
        val firstElement = data[0]
        if (firstElement is PieChartData && firstElement.listMap.isNotEmpty())
            return true
        return false
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        currFilter = NO_FILTER
        fetchData()

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun compare(o1: Data?, o2: Data?): Int {
        if (o1 == null || o2 == null)
            return 0

        if (o1 !is CategoryAnalysisData || o2 !is CategoryAnalysisData)
            return 0

        if (currFilter == NO_FILTER)
            return java.util.Random().nextInt(5) - java.util.Random().nextInt(5)


        return when (currFilter) {
            ASCENDING_ORDER -> {
                (o1.totalExpenseInCategory - o2.totalExpenseInCategory).toInt()
            }

            DESCENDING_ORDER -> {
                (o2.totalExpenseInCategory - o1.totalExpenseInCategory).toInt()
            }

            else ->
                0

        }
    }
}