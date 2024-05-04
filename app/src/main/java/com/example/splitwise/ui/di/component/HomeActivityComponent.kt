package com.example.splitwise.ui.di.component

import com.example.splitwise.ui.HomeActivity
import com.example.splitwise.ui.di.ActivityScope
import com.example.splitwise.ui.di.module.HomeActivityModule
import dagger.Component


@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [HomeActivityModule::class])
interface HomeActivityComponent {
    fun inject(activity: HomeActivity)
}