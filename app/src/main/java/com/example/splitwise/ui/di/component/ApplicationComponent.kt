package com.example.splitwise.ui.di.component

import com.example.splitwise.MyApplication
import com.example.splitwise.ui.di.module.ApplicationModule
import dagger.Component
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
   fun inject(application: MyApplication)

   fun getFirebaseDbInstance() : FirebaseFirestore
   fun getFirebaseAuthInstance() : FirebaseAuth
}