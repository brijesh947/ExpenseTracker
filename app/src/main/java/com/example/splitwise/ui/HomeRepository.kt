package com.example.splitwise.ui

import android.util.Log
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.data.ShoppingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
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
                val userRef = db.collection("users").document(auth.currentUser!!.uid).collection("userDetail")
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
    fun getUserExpenseDetail(groupData: GroupDetailData): Flow<List<ShoppingData>> {
        Log.d("TAGD", "getUserDetail in repo is called")
        return callbackFlow {
            val list: MutableList<ShoppingData> = mutableListOf()
            if (auth.currentUser != null) {
                val userRef = db.collection("users").document(auth.currentUser!!.uid).collection("userDetail").document(groupData.id).collection("expenseDetail")
                userRef.get().addOnSuccessListener { querySnapshot ->
                    for (doc in querySnapshot.documents) {
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

    fun addNewGroupToFirebase(groupData: GroupDetailData): Flow<Boolean> {
        return callbackFlow {
            var isGroupAdded: Boolean
            if (auth.currentUser != null) {
                val userRef = db.collection("users").document(auth.currentUser!!.uid).collection("userDetail")
                val userDetail = hashMapOf("group_name" to groupData.groupName, "total_expense" to groupData.totalExpense)
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
                val userDetail = hashMapOf("name" to expenseData.shoppingName, "type" to expenseData.shoppingCategory,"amount" to expenseData.totalAmount)
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