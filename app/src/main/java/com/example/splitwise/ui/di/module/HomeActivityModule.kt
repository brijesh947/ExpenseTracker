package com.example.splitwise.ui.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.splitwise.MyApplication
import com.example.splitwise.ui.repository.HomeRepository
import com.example.splitwise.ui.viewmodel.HomeViewModel
import com.example.splitwise.ui.di.ActivityContext
import com.example.splitwise.ui.di.ApplicationContext
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

    @ApplicationContext
    @Provides
    fun provideApplicationContext(): MyApplication {
        return activity.applicationContext as MyApplication
    }

    @Provides
    fun provideViewModel(repository: HomeRepository): HomeViewModel {
        return ViewModelProvider(activity, ViewModelProviderFactory(HomeViewModel::class) { HomeViewModel(repository) })[HomeViewModel::class.java]
    }
}