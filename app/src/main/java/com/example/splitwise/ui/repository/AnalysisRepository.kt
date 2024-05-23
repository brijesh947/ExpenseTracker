package com.example.splitwise.ui.repository

import android.util.Log
import com.example.splitwise.data.CategoryAnalysisData
import com.example.splitwise.data.Data
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.data.PieChartData
import com.example.splitwise.data.RecentTransactionData
import com.example.splitwise.ui.util.BEAUTY
import com.example.splitwise.ui.util.BIKE
import com.example.splitwise.ui.util.CLOTHING
import com.example.splitwise.ui.util.DONATE
import com.example.splitwise.ui.util.FOOD
import com.example.splitwise.ui.util.HEALTH
import com.example.splitwise.ui.util.MOBILE
import com.example.splitwise.ui.util.MOVIE
import com.example.splitwise.ui.util.PETROL_PUMP
import com.example.splitwise.ui.util.RENT
import com.example.splitwise.ui.util.SHOPPING_GENERAL
import com.example.splitwise.ui.util.SPORTS
import com.example.splitwise.ui.util.TRANSPORT
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
                        val spending: HashMap<String, List<Pair<Long, Double>>> = hashMapOf()
                        var totalSpending = 0.0

                        for (doc in it.documents) {
                            val key = ("" + doc.get("type")).toUpperCase()
                            val time = ("" + doc.get("time")).toLong()
                            val amount = ("" + doc.get("amount")).toDouble()
                            val pair = Pair(time, amount)

                            if (spending.containsKey(key)) {
                                val updatedList = spending[key]?.toMutableList()
                                updatedList?.add(pair)
                                spending[key] = updatedList ?: listOf(pair)
                            } else {
                                spending[key] = listOf(pair)
                            }

                            totalSpending += amount
                        }


                        val pieData = PieChartData(spending, totalSpending.toString())

                        list.add(pieData)
                        for ((key, value) in pieData.listMap) {
                           list.add(CategoryAnalysisData(key,value,totalSpending.toLong()))
                        }
                    }
                    Log.d("ajdkfgb", "list data is ${list.toList()}:")

                    if (list.size > 1) {
                        list.add(1, RecentTransactionData("Category Wise Analysis"))
                    }
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