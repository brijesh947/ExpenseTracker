package com.example.splitwise.ui.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.splitwise.ui.HomeRepository
import com.example.splitwise.ui.HomeViewModel
import com.example.splitwise.ui.di.ActivityContext
import com.example.splitwise.ui.util.ViewModelProviderFactory
import dagger.Module
import dagger.Provides


@Module
class HomeActivityModule(private val activity : AppCompatActivity) {


    @ActivityContext
    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideViewModel(repository: HomeRepository): HomeViewModel {
        return ViewModelProvider(activity, ViewModelProviderFactory(HomeViewModel::class) { HomeViewModel(repository) })[HomeViewModel::class.java]
    }
}