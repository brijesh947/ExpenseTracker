package com.example.splitwise.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.BudgetBuilder
import com.example.splitwise.MonthYearPickerDialog
import com.example.splitwise.MyApplication
import com.example.splitwise.data.CurrBudgetData
import com.example.splitwise.data.Data
import com.example.splitwise.databinding.BudgetFragmentLayoutBinding
import com.example.splitwise.ui.AnalysisAdapter
import com.example.splitwise.ui.ExpenseDetailActivity
import com.example.splitwise.ui.di.component.DaggerExpenseDetailActivityComponent
import com.example.splitwise.ui.di.module.ExpenseDetailActivityModule
import com.example.splitwise.ui.di.module.HomeActivityModule
import com.example.splitwise.ui.util.UiState
import com.example.splitwise.ui.util.hide
import com.example.splitwise.ui.util.show
import com.example.splitwise.ui.viewmodel.AnalysisViewModel
import com.example.splitwise.ui.viewmodel.BudgetViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class BudgetFragment(val application: MyApplication, val activity: ExpenseDetailActivity) : BaseFragment(), BudgetBuilder {
    private lateinit var binding: BudgetFragmentLayoutBinding

    @Inject
    lateinit var viewModel: BudgetViewModel

    private lateinit var recyclerView: RecyclerView

    private var list = ArrayList<Data>()

    @Inject
    lateinit var adapter: AnalysisAdapter



    private var currMonth = -1
    private var currYear = -1

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
        binding = BudgetFragmentLayoutBinding.inflate(layoutInflater)

        setCurrentMonth()
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        binding.userGroupName.text = getMonthName(currMonth)+ " " + currYear

        binding.monthFilter.setOnClickListener {
            openMonthSelectorDialog()
        }

        adapter.setListener(this)
        return binding.root
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
        fetchData()
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
            fetchData()
            binding.userGroupName.text = getMonthName(currMonth) + " " + currYear
        }
        pd.show(requireFragmentManager(), "MonthYearPickerDialog")
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
        viewModel.getMonthWiseBudgetData(activity.getGroupData(), currMonth, currYear)

        try {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.budgetDetail.collect {
                        when (it) {
                            is UiState.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                                binding.noElement.visibility = View.GONE
                                recyclerView.hide()
                            }

                            is UiState.Success -> {
                                binding.progressBar.visibility = View.GONE
                                if (it.data.isNotEmpty()) {

                                    recyclerView.show()
                                    binding.noElement.visibility = View.GONE
                                    adapter.setList(it.data as ArrayList<Data>)
                                    list = it.data

                                } else {
                                    binding.noElement.visibility = View.VISIBLE
                                    recyclerView.hide()
                                }
                            }

                            else -> {
                                binding.progressBar.visibility = View.GONE
                                binding.noElement.visibility = View.VISIBLE
                                recyclerView.hide()

                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("sdfvfr", "setData: ${e.message}")
        }

    }


    override fun setBudget(data: CurrBudgetData, budget: String, position: Int) {
        val regex = ".*\\d{4}\$".toRegex()
        val sharePrefEditor = requireContext().getSharedPreferences("budget", Context.MODE_PRIVATE).edit()
        var categoryBudget = 0L
        if (regex.matches(data.name)) {
            categoryBudget = budget.toLong()
            sharePrefEditor.putLong("" + currMonth + "_" + currYear, categoryBudget).apply()
        } else {
            categoryBudget =budget.toLong()
            sharePrefEditor.putLong("" + currMonth + "_" + currYear + "_" + data.name, categoryBudget).apply()
        }
        data.totalBudget
        adapter.notifyItemChanged(position)
    }
}