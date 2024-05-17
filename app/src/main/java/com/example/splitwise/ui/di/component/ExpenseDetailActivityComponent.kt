package com.example.splitwise.ui.di.component

import com.example.splitwise.ui.ExpenseDetailActivity
import com.example.splitwise.ui.fragment.RecordsFragment
import com.example.splitwise.ui.di.ActivityScope
import com.example.splitwise.ui.di.module.ExpenseDetailActivityModule
import com.example.splitwise.ui.di.module.HomeActivityModule
import com.example.splitwise.ui.fragment.AnalysisFragment
import com.example.splitwise.ui.fragment.BudgetFragment
import dagger.Component


@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [HomeActivityModule::class, ExpenseDetailActivityModule::class])
interface ExpenseDetailActivityComponent {
    fun inject(activity: ExpenseDetailActivity)
    fun inject(fragment: RecordsFragment)
    fun inject(fragment: AnalysisFragment)
    fun inject(fragment: BudgetFragment)
}