package com.example.splitwise.ui

import android.util.Log
import com.example.splitwise.data.Data
import com.example.splitwise.data.DateData
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.data.MonthData
import com.example.splitwise.data.ShoppingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Calendar
import javax.inject.Inject
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
                val currCalendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                }
                var month = currCalendar.get(Calendar.MONTH)
                var year = currCalendar.get(Calendar.YEAR)
                var date = -1
                val userRef = db.collection("users").document(auth.currentUser!!.uid).collection("userDetail").document(groupData.id).collection("expenseDetail").orderBy("time",Query.Direction.DESCENDING)
                userRef.get().addOnSuccessListener { querySnapshot ->
                    for (doc in querySnapshot.documents) {
                        val currTimeInMills  = doc.get("time")

                        val calendar = Calendar.getInstance().apply {
                            this.timeInMillis = currTimeInMills as Long
                        }

                        if (needToAddDMYData(calendar, month, year, date, Calendar.YEAR)) {
                            year = calendar.get(Calendar.YEAR)
                            //you can add year wise support here.
                        }
                        if (needToAddDMYData(calendar, month, year, date, Calendar.MONTH)) {
                            month = calendar.get(Calendar.MONTH)
                            list.add(MonthData("End of ${getMonthName(month)} $year"))
                        }
                        if (needToAddDMYData(calendar, month, year, date, Calendar.DATE)) {
                            date = calendar.get(Calendar.DATE)
                            list.add(DateData(getDate(date, month), date, month, year))
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
        today+=getMonthName(month)
        return today
    }

    private fun getMonthName(month: Int): String {
        when (month) {
            Calendar.JANUARY ->
                return "JANUARY"

            Calendar.FEBRUARY ->
                return "FEBRUARY"

            Calendar.MARCH ->
                return "MARCH"

            Calendar.APRIL ->
                return "APRIL"

            Calendar.MAY ->
                return "MAY"

            Calendar.JUNE ->
                return "JUNE"

            Calendar.JULY ->
                return "JULY"

            Calendar.AUGUST ->
                return "AUGUST"

            Calendar.SEPTEMBER ->
                return "SEPTEMBER"

            Calendar.OCTOBER ->
                return "OCTOBER"

            Calendar.NOVEMBER ->
                return "NOVEMBER"

            Calendar.DECEMBER ->
                return "DECEMBER"
        }
        return ""
    }

    /*
    * here DMY stands for Date wise ,Month and Year wise Filter
    * */
    private fun needToAddDMYData(calendar: Calendar, month: Int, year: Int, date: Int, filterType: Int): Boolean {
        when (filterType) {
            Calendar.YEAR -> {
                if (calendar.get(Calendar.YEAR) != year)
                    return true
            }

            Calendar.MONTH -> {
                if (calendar.get(Calendar.MONTH) != month)
                    return true
            }

            Calendar.DATE -> {
                if (calendar.get(Calendar.DATE) != date)
                    return true
            }
        }
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

                val calendar = Calendar.getInstance().apply {
                    this.timeInMillis = System.currentTimeMillis()
                }
                val userDetail = hashMapOf(
                    "name" to expenseData.shoppingName,
                    "type" to expenseData.shoppingCategory,
                    "amount" to expenseData.totalAmount,
                    "time" to System.currentTimeMillis(),
                    "date" to calendar.get(Calendar.DATE),
                    "month" to getMonthName(calendar.get(Calendar.MONTH)),
                    "year" to calendar.get(Calendar.YEAR)
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