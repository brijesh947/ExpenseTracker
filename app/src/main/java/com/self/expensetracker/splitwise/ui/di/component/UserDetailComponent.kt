package com.self.expensetracker.splitwise.ui.di.component

import com.self.expensetracker.splitwise.ui.UserDetailActivity
import com.self.expensetracker.splitwise.ui.di.ActivityScope
import com.self.expensetracker.splitwise.ui.di.module.ExpenseDetailActivityModule
import com.self.expensetracker.splitwise.ui.di.module.UserDetailActivityModule
import dagger.Component


@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [UserDetailActivityModule::class])
interface UserDetailComponent {
    fun inject(activity: UserDetailActivity)
}