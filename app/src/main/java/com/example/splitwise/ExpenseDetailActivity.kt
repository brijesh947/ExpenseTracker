package com.example.splitwise

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.databinding.ExpensesDetailLayoutBinding
import com.example.splitwise.ui.di.component.DaggerExpenseDetailActivityComponent
import com.example.splitwise.ui.di.module.ExpenseDetailActivityModule
import com.example.splitwise.ui.di.module.HomeActivityModule
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

    private fun setWindowColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (selectedFragmentPos != 1)
            window.statusBarColor = resources.getColor(R.color.transparent)
        else
            window.statusBarColor = resources.getColor(R.color.top_nav_bar_light)
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
                }

                R.id.budgets -> {
                    selectedFragmentPos = 3
                    selectedFragment = supportFragmentManager.findFragmentByTag("3")
                    if (selectedFragment == null) {
                        selectedFragment = budgetFragment
                    }
                }

                R.id.category -> {
                    selectedFragmentPos = 4
                    selectedFragment = supportFragmentManager.findFragmentByTag("4")
                    if (selectedFragment == null) {
                        selectedFragment = categoryFragment
                    }
                }
            }
            setWindowColor()
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, selectedFragment!!, selectedFragmentPos.toString())
                .commit()
            true
        }
        if(recordsFragment!=null){
            recordsFragment.setGroupData(data)
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