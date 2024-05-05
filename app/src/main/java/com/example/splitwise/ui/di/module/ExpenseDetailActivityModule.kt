package com.example.splitwise.ui.di.module

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.splitwise.ui.fragment.AnalysisFragment
import com.example.splitwise.ui.fragment.BudgetFragment
import com.example.splitwise.ui.fragment.CategoryFragment
import com.example.splitwise.ui.ExpenseDetailActivity
import com.example.splitwise.MyApplication
import com.example.splitwise.ui.AnalysisAdapter
import com.example.splitwise.ui.fragment.RecordsFragment
import com.example.splitwise.ui.HomeAdapter
import com.example.splitwise.ui.di.ActivityContext
import com.example.splitwise.ui.repository.AnalysisRepository
import com.example.splitwise.ui.util.ViewModelProviderFactory
import com.example.splitwise.ui.viewmodel.AnalysisViewModel
import dagger.Module
import dagger.Provides


@Module
class ExpenseDetailActivityModule(private val application: MyApplication, private val activity: ExpenseDetailActivity) {

    @ActivityContext
    @Provides
    fun getContext(): Context {
        return activity
    }


    @Provides
    fun getRecordsFragment(): RecordsFragment {
        return RecordsFragment(application, activity)
    }

    @Provides
    fun getCategoryFragment(): CategoryFragment {
        return CategoryFragment()
    }

    @Provides
    fun getBudgetFragment(): BudgetFragment {
        return BudgetFragment()
    }

    @Provides
    fun getAnalysisFragment(): AnalysisFragment {
        return AnalysisFragment(application, activity)
    }

    @Provides
    fun getHomeAdapter(): HomeAdapter {
        return HomeAdapter(activity, application)
    }

    @Provides
    fun getAnalysisAdapter(): AnalysisAdapter {
        return AnalysisAdapter()
    }

    @Provides
    fun provideAnalysisViewModel(repository: AnalysisRepository): AnalysisViewModel {
        return ViewModelProvider(activity,
            ViewModelProviderFactory(AnalysisViewModel::class) { AnalysisViewModel(repository) })[AnalysisViewModel::class.java]
    }


}