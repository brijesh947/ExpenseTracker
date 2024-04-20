package com.example.splitwise

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.example.splitwise.databinding.ExpensesDetailLayoutBinding

class ExpenseDetailActivity :AppCompatActivity(){
    private lateinit var binding: ExpensesDetailLayoutBinding
    private lateinit var recordsFragment: RecordsFragment
    private lateinit var analysisFragment: AnalysisFragment
    private lateinit var budgetFragment: BudgetFragment
    private lateinit var categoryFragment: CategoryFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ExpensesDetailLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowColor()
        binding.bottomNavigation.itemIconTintList = null
        initFragment();

    }
    private var selectedFragmentPos = 1
    private var selectedFragment :Fragment? = null

    private fun initFragment() {
        recordsFragment = RecordsFragment()
        categoryFragment = CategoryFragment()
        analysisFragment = AnalysisFragment()
        budgetFragment = BudgetFragment()
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