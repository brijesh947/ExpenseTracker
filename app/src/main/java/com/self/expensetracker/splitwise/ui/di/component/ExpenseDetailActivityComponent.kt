package com.self.expensetracker.splitwise.ui.di.component

import com.self.expensetracker.splitwise.ui.ExpenseDetailActivity
import com.self.expensetracker.splitwise.ui.fragment.RecordsFragment
import com.self.expensetracker.splitwise.ui.di.ActivityScope
import com.self.expensetracker.splitwise.ui.di.module.ExpenseDetailActivityModule
import com.self.expensetracker.splitwise.ui.di.module.HomeActivityModule
import com.self.expensetracker.splitwise.ui.fragment.AnalysisFragment
import com.self.expensetracker.splitwise.ui.fragment.BaseFragment
import com.self.expensetracker.splitwise.ui.fragment.BudgetFragment
import dagger.Component


@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [HomeActivityModule::class, ExpenseDetailActivityModule::class])
interface ExpenseDetailActivityComponent {
    fun inject(activity: ExpenseDetailActivity)
    fun inject(fragment: RecordsFragment)
    fun inject(fragment: AnalysisFragment)
    fun inject(fragment: BudgetFragment)
    fun inject(fragment: BaseFragment)
}