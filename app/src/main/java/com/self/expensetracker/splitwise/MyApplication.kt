package com.self.expensetracker.splitwise

import android.app.Application
import com.self.expensetracker.splitwise.ui.di.component.ApplicationComponent
import com.self.expensetracker.splitwise.ui.di.component.DaggerApplicationComponent
import com.self.expensetracker.splitwise.ui.di.module.ApplicationModule
import com.google.firebase.FirebaseApp
import javax.inject.Inject

class MyApplication @Inject constructor() : Application() {
    lateinit var applicationComponent: ApplicationComponent
    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        injectDaggerDependencies()
        super.onCreate()
    }

    private fun injectDaggerDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }

}