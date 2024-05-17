package com.example.splitwise.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise.data.Data
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.ui.repository.AnalysisRepository
import com.example.splitwise.ui.repository.BudgetRepository
import com.example.splitwise.ui.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class BudgetViewModel @Inject constructor(val analysisRepository: AnalysisRepository, val budgetRepository: BudgetRepository) : ViewModel() {

    private var _budgetDetail = MutableStateFlow<UiState<List<Data>>>(UiState.Loading)
    val budgetDetail: StateFlow<UiState<List<Data>>> get() = _budgetDetail


    fun getMonthWiseBudgetData(groupDetailData: GroupDetailData, month: Int, year: Int) {
        _budgetDetail.value = UiState.Loading
        viewModelScope.launch {
           // Log.d("TAGD", "getMonthWiseBudgetData  in view model  is called")
            analysisRepository.getUserExpenseDetail(groupDetailData, month, year).catch { e ->
               Log.d("TAGD", "response from analysis repository error catch in flow ${e.message}")
                _budgetDetail.value = UiState.Error(e.toString())
            }.collect {
               Log.d("TAGD", "response from analysis repository collected in flow ${it.size}")
                if (it.size <= 1)
                    _budgetDetail.value = UiState.Error("getMonthWiseBudgetData Detail is Empty")
                else {

                    // here you will get each budget category's expense and based on that you should call budget repository's method
                    // which will excess each category/month budget from sharedPref

                    budgetRepository.getBudgetDataForMonthAndCategory(month, year, it).catch { e ->
                        _budgetDetail.value = UiState.Error(e.toString())
                        Log.d("TAGD", "error catch in flow ${e.message}")
                    }.collect {
                        Log.d("TAGD", "response  from budgetRepository ${it.toList()}")
                        _budgetDetail.value = UiState.Success(it)
                    }

                }
            }
        }

    }



}