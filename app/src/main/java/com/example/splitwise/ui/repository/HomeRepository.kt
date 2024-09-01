package com.example.splitwise.ui.repository

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.splitwise.MyApplication
import com.example.splitwise.data.Data
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.data.MonthWiseProgressData
import com.example.splitwise.data.RecentTransactionData
import com.example.splitwise.data.ShoppingData
import com.example.splitwise.ui.util.CategoryManager
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

class HomeRepository @Inject constructor(private val db: FirebaseFirestore, private val auth: FirebaseAuth,private val application:MyApplication) : BaseRepository() {
    private val instance = CategoryManager.getInstance()

    fun getUserDetail(): Flow<List<GroupDetailData>> {
        Log.d("TAGD", "getUserDetail in repo is called")
        return callbackFlow {
            val list: MutableList<GroupDetailData> = mutableListOf()
            if (auth.currentUser != null) {
                val userRef = db.collection("users").document(auth.currentUser!!.uid).collection("userDetail").orderBy("time",Query.Direction.DESCENDING)
                userRef.get().addOnSuccessListener { querySnapshot ->
                    for (doc in querySnapshot.documents) {
                        var group = "Home"
                        if (doc.contains("type"))
                            group = "" + doc.get("type")

                        val tempData = GroupDetailData(doc.id,"" + doc.get("group_name"), group, "" + doc.get("total_expense"))
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

    fun getUserPersonalDetail(): Flow<List<String>> {

        return callbackFlow {
            val list: MutableList<String> = mutableListOf()
            if (auth.currentUser != null) {
                val userRef = db.collection("users").document(auth.currentUser!!.uid).collection("userPersonalDetail")

                userRef.get().addOnSuccessListener { querySnapshot ->
                    for (doc in querySnapshot.documents) {
                        list.add("" + doc.get("name"))
                        list.add("" + doc.get("email"))
                        list.add("" + doc.get("password"))
                    }
                    Log.d("wkhrl", "getUserPersonalDetail: is success and list is ${list.toList()}")
                    trySend(list)
                }.addOnFailureListener {

                    Log.d("wkhrl", "getUserPersonalDetail: is failed and the reason is ${it.message}")
                    trySend(list)
                }
            }
            awaitClose { }
        }
    }


    fun getUserExpenseDetail(groupData: GroupDetailData,currMonth:Int,currYear:Int): Flow<List<Data>> {
        Log.d("TAGD", "getUserDetail in repo is called")
        return callbackFlow {
            val list: MutableList<Data> = mutableListOf()
            if (auth.currentUser != null) {
                val currCalendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                }
                var month: Int
                var year: Int
                var date = -1
                var totalSum :Long = 0
                val userRef =
                    db.collection("users").document(auth.currentUser!!.uid).collection("userDetail")
                        .document(groupData.id).collection("expenseDetail")
                        .whereEqualTo("month", getMonthName(currMonth))
                        .whereEqualTo("year", currYear).orderBy("time", Query.Direction.DESCENDING)
                userRef.get().addOnSuccessListener { querySnapshot ->
                    for (doc in querySnapshot.documents) {
                        val currTimeInMills  = doc.get("time")
                        val calendar = Calendar.getInstance().apply {
                            this.timeInMillis = currTimeInMills as Long
                        }
                        year = calendar.get(Calendar.YEAR)
                        month = calendar.get(Calendar.MONTH)
                        date = calendar.get(Calendar.DATE)
                        totalSum += (doc.get("amount") as String).toDouble().toLong()
                        var comment = ""
                        if (doc.contains("comment")) {
                            comment = "" + doc.get("comment")
                        }
                        var categoryType = ""
                        if(doc.contains("categoryType")){
                            categoryType = "" + doc.get("categoryType")
                        }
                        Log.d("djkvf", "Date is ${calendar.get(Calendar.DATE)} and Month is ${calendar.get(Calendar.MONTH)+1} and the year is ${calendar.get(Calendar.YEAR)}")
                        val tempData = ShoppingData(doc.id,"" + doc.get("name"),""+ doc.get("type"),categoryType,""+doc.get("amount"),month,year,"" + date,comment)
                        list.add(tempData)
                    }
                    if (list.isNotEmpty()) {
                        list.add(0, RecentTransactionData("Recent Transaction"))
                        list.add(0, MonthWiseProgressData(50000, totalSum))
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


    fun getTotalCategoryType(groupData: GroupDetailData):Flow<Boolean>{

        instance.clearMap()

        return callbackFlow {
            if(auth.currentUser!=null){
                val userRef = db.collection("users").document(auth.currentUser!!.uid).collection("userDetail").document(groupData.id).collection("categoryDetail")

                userRef.get().addOnSuccessListener {

                    for (doc in it.documents) {
                        instance.createNewCategory(
                            "" + doc.get("type"),
                            "" + doc.get("name"),
                            "",
                            "" + doc.get("id")
                        )
                    }
                    trySend(true)
                }.addOnFailureListener {
                    trySend(false)
                }

            }

            awaitClose{

            }

        }
    }

    fun addNewCategoryType(groupData: GroupDetailData, categoryData: CategoryManager.CategoryHolderData): Flow<Boolean> {
        return callbackFlow {
            if (auth.currentUser != null) {
                val userRef =
                    db.collection("users").document(auth.currentUser!!.uid).collection("userDetail")
                        .document(groupData.id).collection("categoryDetail")

                val categoryDetail = hashMapOf(
                    "name" to categoryData.categoryName,
                    "type" to categoryData.type
                )

                userRef.add(categoryDetail).addOnSuccessListener {
                    instance.createNewCategory("" + categoryData.type,categoryData.categoryName,"","")
                    trySend(true)
                }.addOnFailureListener {
                    trySend(false)
                }
            }
            awaitClose {  }
        }
    }

    fun addNewGroupToFirebase(groupData: GroupDetailData): Flow<Boolean> {
        return callbackFlow {
            var isGroupAdded: Boolean
            if (auth.currentUser != null) {
                val userRef = db.collection("users").document(auth.currentUser!!.uid).collection("userDetail")
                val userDetail = hashMapOf(
                    "group_name" to groupData.groupName,
                    "total_expense" to groupData.totalExpense,
                    "type" to groupData.groupType,
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
                    "categoryType" to expenseData.shoppingCategoryType,
                    "amount" to expenseData.totalAmount,
                    "time" to System.currentTimeMillis(),
                    "date" to calendar.get(Calendar.DATE),
                    "month" to getMonthName(calendar.get(Calendar.MONTH)),
                    "year" to calendar.get(Calendar.YEAR),
                    "comment" to expenseData.comment
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
    fun updateExpenseToFirebase(groupData: GroupDetailData,expenseData: ShoppingData): Flow<Boolean> {
        return callbackFlow {
            var isGroupUpdated: Boolean
            if (auth.currentUser != null) {
                val userRef =
                    db.collection("users").document(auth.currentUser!!.uid).collection("userDetail")
                        .document(groupData.id).collection("expenseDetail").document(expenseData.id)


                val userDetail = hashMapOf<String,Any>(
                    "name" to expenseData.shoppingName,
                    "type" to expenseData.shoppingCategory,
                    "categoryType" to expenseData.shoppingCategoryType,
                    "amount" to expenseData.totalAmount,
                    "comment" to expenseData.comment
                )
                userRef.update(userDetail).addOnSuccessListener {
                    isGroupUpdated = true
                    trySend(isGroupUpdated)
                    Log.d("TAGD", "expense added successFully")
                }.addOnFailureListener {
                    isGroupUpdated = false
                    trySend(isGroupUpdated)
                    Log.d("TAGD", "failed due to ${it.message}")
                }
            }
            awaitClose {  }
        }
    }
    fun deleteExpenseToFirebase(groupData: GroupDetailData,expenseData: ShoppingData): Flow<Boolean> {
        return callbackFlow {
            var isGroupUpdated: Boolean
            if (auth.currentUser != null) {
                val userRef =
                    db.collection("users").document(auth.currentUser!!.uid).collection("userDetail")
                        .document(groupData.id).collection("expenseDetail").document(expenseData.id)


                userRef.delete().addOnSuccessListener {
                    isGroupUpdated = true
                    trySend(isGroupUpdated)
                    Log.d("TAGD", "expense added successFully")
                }.addOnFailureListener {
                    isGroupUpdated = false
                    trySend(isGroupUpdated)
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