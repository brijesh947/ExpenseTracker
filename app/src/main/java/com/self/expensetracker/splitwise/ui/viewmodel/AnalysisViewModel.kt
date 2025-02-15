package com.self.expensetracker.splitwise.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.data.GroupDetailData
import com.self.expensetracker.splitwise.ui.repository.AnalysisRepository
import com.self.expensetracker.splitwise.ui.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class AnalysisViewModel @Inject constructor(private val repository: AnalysisRepository) : ViewModel() {

    private var _expenseDetail = MutableStateFlow<UiState<List<Data>>>(UiState.Loading)
    val expenseDetail : StateFlow<UiState<List<Data>>> get() = _expenseDetail


    fun getMonthWiseData(groupDetailData: GroupDetailData, month: Int, year: Int) {
        _expenseDetail.value = UiState.Loading
        viewModelScope.launch {
            Log.d("TAGD", "getUserDetail in view model  is called")
            repository.getUserExpenseDetail(groupDetailData,month,year).catch { e ->
                Log.d("TAGD", "error catch in flow ${e.message}")
                _expenseDetail.value = UiState.Error(e.toString())
            }.collect {
                Log.d("TAGD", "response collected in flow ${it.size}")
                if (it.isEmpty())
                    _expenseDetail.value = UiState.Error("User Detail is Empty")
                else
                    _expenseDetail.value = UiState.Success(it)
            }
        }

    }


}