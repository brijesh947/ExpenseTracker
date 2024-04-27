package com.example.splitwise.ui

import android.util.Log
import com.example.splitwise.data.Data
import com.example.splitwise.data.DateData
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.data.ShoppingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class HomeRepository @Inject constructor(private val db: FirebaseFirestore, private val auth: FirebaseAuth) {
    fun getUserDetail(): Flow<List<GroupDetailData>> {
        Log.d("TAGD", "getUserDetail in repo is called")
        return callbackFlow {
            val list: MutableList<GroupDetailData> = mutableListOf()
            if (auth.currentUser != null) {
                val userRef = db.collection("users").document(auth.currentUser!!.uid).collection("userDetail").orderBy("time",Query.Direction.DESCENDING)
                userRef.get().addOnSuccessListener { querySnapshot ->
                    for (doc in querySnapshot.documents) {
                        val tempData = GroupDetailData(doc.id,"" + doc.get("group_name"), "Home", "" + doc.get("total_expense"))
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
    fun getUserExpenseDetail(groupData: GroupDetailData): Flow<List<Data>> {
        Log.d("TAGD", "getUserDetail in repo is called")
        return callbackFlow {
            val list: MutableList<Data> = mutableListOf()
            if (auth.currentUser != null) {
                var month = -1
                var year  = -1
                var date = -1
                val userRef = db.collection("users").document(auth.currentUser!!.uid).collection("userDetail").document(groupData.id).collection("expenseDetail").orderBy("time",Query.Direction.DESCENDING)
                userRef.get().addOnSuccessListener { querySnapshot ->
                    for (doc in querySnapshot.documents) {
                        val currTimeInMills  = doc.get("time")
                        val calendar = Calendar.getInstance().apply {
                            this.timeInMillis = currTimeInMills as Long
                        }
                        if (needToAddDateDate(calendar, month, year, date)) {
                            date = calendar.get(Calendar.DATE)
                            year = calendar.get(Calendar.YEAR)
                            month = calendar.get(Calendar.MONTH)
                            list.add(DateData(getDate(date,month),date,month,year))
                        }
                        Log.d("djkvf", "Date is ${calendar.get(Calendar.DATE)} and Month is ${calendar.get(Calendar.MONTH)+1} and the year is ${calendar.get(Calendar.YEAR)}")
                        val tempData = ShoppingData(doc.id,"" + doc.get("name"),""+ doc.get("type"),""+doc.get("amount"))
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

    private fun getDate(date: Int, month: Int): String {
        var today = "$date "
        when (month) {
            Calendar.JANUARY ->
                today += "JANUARY"

            Calendar.FEBRUARY ->
                today += "FEBRUARY"

            Calendar.MARCH ->
                today += "MARCH"

            Calendar.APRIL ->
                today += "APRIL"

            Calendar.MAY ->
                today += "MAY"

            Calendar.JUNE ->
                today += "JUNE"

            Calendar.JULY ->
                today += "JULY"

            Calendar.AUGUST ->
                today += "AUGUST"

            Calendar.SEPTEMBER ->
                today += "SEPTEMBER"

            Calendar.OCTOBER ->
                today += "OCTOBER"

            Calendar.NOVEMBER ->
                today += "NOVEMBER"

            Calendar.DECEMBER ->
                today += "DECEMBER"
        }
        return today
    }

    private fun needToAddDateDate(calendar: Calendar, month: Int, year: Int, date: Int): Boolean {

        if (calendar.get(Calendar.DATE) != date)
            return true
        if (calendar.get(Calendar.YEAR) != year)
            return true
        if (calendar.get(Calendar.MONTH) != month)
            return true

        return false

    }

    fun addNewGroupToFirebase(groupData: GroupDetailData): Flow<Boolean> {
        return callbackFlow {
            var isGroupAdded: Boolean
            if (auth.currentUser != null) {
                val userRef = db.collection("users").document(auth.currentUser!!.uid).collection("userDetail")
                val userDetail = hashMapOf(
                    "group_name" to groupData.groupName,
                    "total_expense" to groupData.totalExpense,
                    "time" to System.currentTimeMillis()
                )
                userRef.add(userDetail).addOnSuccessListener {
                    isGroupAdded = true
                    groupData.id = it.id
                    trySend(isGroupAdded)
                    Log.d("TAGD", "user added successFully")
                }.addOnFailureListener {
                    isGroupAdded = false
                    trySend(isGroupAdded)
                    Log.d("TAGD", "failed due to ${it.message}")
                }
            }
            awaitClose {  }
        }

    }
    fun addNewExpenseToFirebase(groupData: GroupDetailData,expenseData: ShoppingData): Flow<Boolean> {
        return callbackFlow {
            var isGroupAdded: Boolean
            if (auth.currentUser != null) {
                val userRef = db.collection("users").document(auth.currentUser!!.uid).collection("userDetail").document(groupData.id).collection("expenseDetail")
                val userDetail = hashMapOf(
                    "name" to expenseData.shoppingName,
                    "type" to expenseData.shoppingCategory,
                    "amount" to expenseData.totalAmount,
                    "time" to System.currentTimeMillis()
                )
                userRef.add(userDetail).addOnSuccessListener {
                    isGroupAdded = true
                    expenseData.id = it.id
                    trySend(isGroupAdded)
                    Log.d("TAGD", "expense added successFully")
                }.addOnFailureListener {
                    isGroupAdded = false
                    trySend(isGroupAdded)
                    Log.d("TAGD", "failed due to ${it.message}")
                }
            }
            awaitClose {  }
        }
    }
    suspend fun updateTotalExpensesInGroup(groupData: GroupDetailData, totalExpenses: String) :Boolean {
        return suspendCoroutine { continuation ->
            if (auth.currentUser != null) {
                val userRef = db.collection("users").document(auth.currentUser!!.uid).collection("userDetail").document(groupData.id)

                db.runTransaction { transaction ->
                    transaction.update(userRef, "total_expense", totalExpenses)
                }.addOnSuccessListener {
                    continuation.resume(true)
                }.addOnFailureListener {
                    Log.d("jfdks", "updateTotalExpensesInGroup: fail $it")
                    continuation.resume(false)
                }

            } else {
                Log.d("jfdks", "updateTotalExpense auth is null")
                continuation.resume(false)
            }
        }
    }

    fun deleteGroupFromFirebase(groupData: GroupDetailData): Flow<Boolean> {
        return callbackFlow {
            if (auth.currentUser != null) {
                val userRef = db.collection("users").document(auth.currentUser!!.uid).collection("userDetail").document(groupData.id)
                userRef.delete().addOnSuccessListener {
                    trySend(true)
                }.addOnFailureListener {
                    trySend(false)
                }
            } else
                trySend(false)

            awaitClose { }
        }
    }

}