package com.self.expensetracker.splitwise.ui.repository

import android.content.Context
import android.util.Log
import com.self.expensetracker.splitwise.data.CurrBudgetData
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.data.PieChartData
import com.self.expensetracker.splitwise.data.RecentTransactionData
import com.self.expensetracker.splitwise.ui.util.CategoryManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class BudgetRepository @Inject constructor(private val context: Context) : BaseRepository() {

    private val categoryManager: CategoryManager = CategoryManager.getInstance()

    fun getBudgetDataForMonthAndCategory(month: Int, year: Int, list1: List<Data>): Flow<List<Data>> {
        return callbackFlow {

            val list: MutableList<Data> = mutableListOf()

            try {

                val monthDefaultBudget: Long = 50000
                val categoryBudget: Long = 10000

                val pieDataMap = (list1[0] as PieChartData).listMap
                val totalExpense = (list1[0] as PieChartData).total

                val sharePref = context.getSharedPreferences("budget", Context.MODE_PRIVATE)


                categoryManager.getTotalCategoryList().forEach {
                    val key = "" + it.type + "_" + it.categoryName
                    if (pieDataMap.containsKey(key = key))
                        list.add(CurrBudgetData(key, sharePref.getLong(("" + month + "_" + year + "_" + key), categoryBudget), getTotalExpense(pieDataMap[key]!!)))
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

    private fun getTotalExpense(list: List<Pair<Long, Double>>): Double {
        var expense = 0.0
        for (item in list) {
            expense += item.second
        }
        return expense
    }

}