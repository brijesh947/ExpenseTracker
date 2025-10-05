package com.self.expensetracker.splitwise.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.self.expensetracker.splitwise.MyApplication
import com.self.expensetracker.splitwise.R
import com.self.expensetracker.splitwise.data.GroupDetailData
import com.self.expensetracker.splitwise.databinding.ExpensesDetailLayoutBinding
import com.self.expensetracker.splitwise.ui.di.component.DaggerExpenseDetailActivityComponent
import com.self.expensetracker.splitwise.ui.di.module.ExpenseDetailActivityModule
import com.self.expensetracker.splitwise.ui.di.module.HomeActivityModule
import com.self.expensetracker.splitwise.ui.fragment.AnalysisFragment
import com.self.expensetracker.splitwise.ui.fragment.BudgetFragment
import com.self.expensetracker.splitwise.ui.fragment.CategoryFragment
import com.self.expensetracker.splitwise.ui.fragment.MonthManager
import com.self.expensetracker.splitwise.ui.fragment.RecordsFragment
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
        // Enable edge-to-edge layout
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Apply system bar colors
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_bar_background)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.app_bar_background_2)

        // Handle icon contrast (light/dark)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = true   // dark text/icons on light bar
        insetsController.isAppearanceLightNavigationBars = false // light icons on dark bar

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            v.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = bars.bottom,
            )
            WindowInsetsCompat.CONSUMED
        }
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