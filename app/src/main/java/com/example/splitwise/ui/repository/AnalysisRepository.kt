package com.example.splitwise.ui.repository

import android.util.Log
import com.example.splitwise.data.Data
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.data.PieChartData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AnalysisRepository @Inject constructor(private val db: FirebaseFirestore, private val auth: FirebaseAuth) : BaseRepository() {

    fun getUserExpenseDetail(groupData: GroupDetailData, month: Int, year: Int): Flow<List<Data>> {
        Log.d("ajdkfgb", "getUserDetail in repo is called")
        return callbackFlow {
            val list: MutableList<Data> = mutableListOf()
            if (auth.currentUser != null) {
                val userRef =
                    db.collection("users").document(auth.currentUser!!.uid).collection("userDetail")
                        .document(groupData.id).collection("expenseDetail")
                        .whereEqualTo("month", getMonthName(month))
                        .whereEqualTo("year", year)
                        .orderBy("time", Query.Direction.DESCENDING)

                userRef.get().addOnSuccessListener { snapShot ->
                    snapShot?.let {
                        val spending: HashMap<String, Double> = hashMapOf()
                        var totalSpending = 0.0
                        for (doc in it.documents) {
                            val key = "" + doc.get("type")
                            if (spending.containsKey(key.toUpperCase()))
                                spending[key.toUpperCase()] = spending[key.toUpperCase()]!! + ("" + doc.get("amount")).toDouble()
                            else
                                spending[key.toUpperCase()] = ("" + doc.get("amount")).toDouble()
                            totalSpending += ("" + doc.get("amount")).toDouble()
                        }

                        list.add(PieChartData(spending, totalSpending.toString()))
                    }
                    Log.d("ajdkfgb", "list data is ${list.toList().toString()}: ")
                    trySend(list)
                }.addOnFailureListener {
                    trySend(list)
                    Log.d("ajdkfgb", "doc is ${it.message}")
                }
            } else {
                Log.d("TAGD", "user is null from firestore")
                trySend(list)
            }
            awaitClose { }
        }
    }

}