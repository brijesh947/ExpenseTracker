package com.self.expensetracker.splitwise.ui.di.component

import com.self.expensetracker.splitwise.ui.HomeActivity
import com.self.expensetracker.splitwise.ui.di.ActivityScope
import com.self.expensetracker.splitwise.ui.di.module.HomeActivityModule
import dagger.Component


@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [HomeActivityModule::class])
interface HomeActivityComponent {
    fun inject(activity: HomeActivity)
}