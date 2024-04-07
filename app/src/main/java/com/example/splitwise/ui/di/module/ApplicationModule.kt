package com.example.splitwise.ui.di.module

import android.content.Context
import com.example.splitwise.MyApplication
import com.example.splitwise.ui.di.ApplicationContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ApplicationModule(private val application: MyApplication) {

    @ApplicationContext
    @Provides
    fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideFirebaseDbInstance(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuthInstance(): FirebaseAuth = FirebaseAuth.getInstance()
}