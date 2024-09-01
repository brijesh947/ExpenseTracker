package com.example.splitwise.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.splitwise.MyApplication
import com.example.splitwise.R
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.databinding.ExpensesDetailLayoutBinding
import com.example.splitwise.ui.di.component.DaggerExpenseDetailActivityComponent
import com.example.splitwise.ui.di.module.ExpenseDetailActivityModule
import com.example.splitwise.ui.di.module.HomeActivityModule
import com.example.splitwise.ui.fragment.AnalysisFragment
import com.example.splitwise.ui.fragment.BudgetFragment
import com.example.splitwise.ui.fragment.CategoryFragment
import com.example.splitwise.ui.fragment.MonthManager
import com.example.splitwise.ui.fragment.RecordsFragment
import javax.inject.Inject

class ExpenseDetailActivity :AppCompatActivity(){

    private lateinit var binding: ExpensesDetailLayoutBinding

    @Inject
    lateinit var recordsFragment: RecordsFragment

    @Inject
    lateinit var analysisFragment: AnalysisFragment

    @Inject
    lateinit var budgetFragment: BudgetFragment

    @Inject
    lateinit var categoryFragment: CategoryFragment

    private var data: GroupDetailData? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        initFragment();
        super.onCreate(savedInstanceState)
        binding = ExpensesDetailLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowColor()
        binding.bottomNavigation.itemIconTintList = null
        intent.extras?.let {
            data = GroupDetailData(it.getString("id")!!,it.getString("name")!!,"",it.getString("expenses")!!)
        }
        MonthManager.getInstance().resetMonthAndYear()
    }
    private var selectedFragmentPos = 1
    private var selectedFragment :Fragment? = null

    private fun initFragment() {
        DaggerExpenseDetailActivityComponent.builder()
            .applicationComponent((application as MyApplication).applicationComponent)
            .homeActivityModule(HomeActivityModule(this as AppCompatActivity))
            .expenseDetailActivityModule(ExpenseDetailActivityModule(application as MyApplication, this))
            .build()
            .inject(this)
    }

    fun getGroupData(): GroupDetailData {
        return data!!
    }

    private fun setWindowColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        window.statusBarColor = resources.getColor(R.color.app_bar_background)
    }

    override fun onResume() {
        super.onResume()
        selectCurrentFragment()
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.records -> {
                    selectedFragmentPos = 1
                    selectedFragment = supportFragmentManager.findFragmentByTag("1")
                    if (selectedFragment == null) {
                        selectedFragment = recordsFragment
                    }
                }

                R.id.analysis -> {
                    selectedFragmentPos = 2
                    selectedFragment = supportFragmentManager.findFragmentByTag("2")
                    if (selectedFragment == null) {
                        selectedFragment = analysisFragment
                    }
                    recordsFragment.isSearchOpen = false
                }

                R.id.budgets -> {
                    selectedFragmentPos = 3
                    selectedFragment = supportFragmentManager.findFragmentByTag("3")
                    if (selectedFragment == null) {
                        selectedFragment = budgetFragment
                    }
                    recordsFragment.isSearchOpen = false

                }

                R.id.category -> {
                    selectedFragmentPos = 4
                    selectedFragment = supportFragmentManager.findFragmentByTag("4")
                    if (selectedFragment == null) {
                        selectedFragment = categoryFragment
                    }
                    recordsFragment.isSearchOpen = false

                }
            }
            setWindowColor()
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, selectedFragment!!, selectedFragmentPos.toString())
                .commit()
            true
        }
    }

    private fun selectCurrentFragment() {
        when (selectedFragmentPos) {
            1 -> supportFragmentManager.beginTransaction().replace(binding.container.id, recordsFragment, "1").commit()

            2 -> supportFragmentManager.beginTransaction().replace(binding.container.id, analysisFragment, "2").commit()

            3 -> supportFragmentManager.beginTransaction().replace(binding.container.id, budgetFragment, "3").commit()

            4 -> supportFragmentManager.beginTransaction().replace(binding.container.id, categoryFragment, "4").commit()
        }
    }
}