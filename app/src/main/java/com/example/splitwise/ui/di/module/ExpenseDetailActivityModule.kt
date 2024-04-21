package com.example.splitwise.ui.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.splitwise.AnalysisFragment
import com.example.splitwise.BudgetFragment
import com.example.splitwise.CategoryFragment
import com.example.splitwise.ExpenseDetailActivity
import com.example.splitwise.MyApplication
import com.example.splitwise.RecordsFragment
import com.example.splitwise.ui.HomeAdapter
import com.example.splitwise.ui.di.ActivityContext
import dagger.Module
import dagger.Provides


@Module
class ExpenseDetailActivityModule(private val application: MyApplication,private val activity: ExpenseDetailActivity) {

    @ActivityContext
    @Provides
    fun getContext(): Context {
        return activity
    }


    @Provides
    fun getRecordsFragment(): RecordsFragment {
        return RecordsFragment(application,activity)
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
        return AnalysisFragment()
    }

    @Provides
    fun getHomeAdapter(): HomeAdapter {
        return HomeAdapter(activity, application)
    }

}