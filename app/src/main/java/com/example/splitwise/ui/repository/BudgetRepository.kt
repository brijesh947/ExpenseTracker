package com.example.splitwise.ui.repository

import android.content.Context
import android.util.Log
import com.example.splitwise.data.CurrBudgetData
import com.example.splitwise.data.Data
import com.example.splitwise.data.PieChartData
import com.example.splitwise.data.RecentTransactionData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class BudgetRepository @Inject constructor(private val context: Context) : BaseRepository() {

    fun getBudgetDataForMonthAndCategory(month: Int, year: Int, list1: List<Data>): Flow<List<Data>> {
        return callbackFlow {

            val list: MutableList<Data> = mutableListOf()

            try {

                val monthDefaultBudget: Long = 50000
                val categoryBudget: Long = 10000

                val pieDataMap = (list1[0] as PieChartData).listMap
                val totalExpense = (list1[0] as PieChartData).total

                val sharePref = context.getSharedPreferences("budget", Context.MODE_PRIVATE)

                for (index in 101..113) {
                    val key = getCategoryType(index)
                    if (pieDataMap.containsKey(key = key))
                        list.add(CurrBudgetData(key, sharePref.getLong(("" + month + "_" + year + "_" + key), categoryBudget), pieDataMap[key]!!))
                    else
                        list.add(CurrBudgetData(key, sharePref.getLong(("" + month + "_" + year + "_" + key), categoryBudget), 0.0))
                }

                list.add(0,RecentTransactionData("Budget Categories: ${getMonthShortName(month)}-$year"))
                list.add(0, CurrBudgetData("" + getMonthShortName(month) + " " + year, sharePref.getLong("" + month + "_" + year, monthDefaultBudget), totalExpense.toDouble()))
                list.add(0,RecentTransactionData("${getMonthShortName(month)}-$year Budget"))

                trySend(list)
            } catch (e: Exception) {
                Log.d("TAGD", "exception in repo ${e.toString()}")
                trySend(list)
            }

            finally {
                awaitClose {  }
            }


        }
    }

}