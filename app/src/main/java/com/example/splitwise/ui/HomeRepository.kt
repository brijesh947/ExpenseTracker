package com.example.splitwise.ui

import android.util.Log
import com.example.splitwise.data.GroupDetailData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

class HomeRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    fun getUserDetail(): Flow<List<GroupDetailData>> {
        Log.d("TAGD", "getUserDetail in repo is called")
        return callbackFlow {
            val list: MutableList<GroupDetailData> = mutableListOf()
            if (auth.currentUser != null) {
                val userRef = db.collection("users").document(auth.currentUser!!.uid).collection("userDetail")
                userRef.get().addOnSuccessListener { querySnapshot ->
                    for (doc in querySnapshot.documents) {
                        val tempData = GroupDetailData("" + doc.get("group_name"), "Home", "" + doc.get("total_expense"))
                        list.add(tempData)
                    }
                    trySend(list)
                }.addOnFailureListener { exception ->
                    Log.d("TAGD", "exception while reading the firestore data ${exception.message}")
                    trySend(list)
                }
            } else {
                Log.d("TAGD", "user is null from firestore")
                trySend(list)
            }
            awaitClose {  }
        }
    }

}