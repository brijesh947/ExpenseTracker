package com.example.splitwise.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise.FirebaseCallback
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.ui.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private var _groupDetail = MutableStateFlow<UiState<List<GroupDetailData>>>(UiState.Loading)
    val groupDetail: StateFlow<UiState<List<GroupDetailData>>> get() = _groupDetail
    fun getUserDetail() {
        viewModelScope.launch {
            Log.d("TAGD", "getUserDetail in view model  is called")
            repository.getUserDetail().catch { e ->
                Log.d("TAGD", "error catch in flow ${e.message}")
                _groupDetail.value = UiState.Error(e.toString())
            }.collect {
                Log.d("TAGD", "response collected in flow ${it.size}")
                if (it.isEmpty())
                    _groupDetail.value = UiState.Error("User Detail is Empty")
                else
                    _groupDetail.value = UiState.Success(it)
            }
        }
    }

    fun addNewUserGroup(data: GroupDetailData, callback: FirebaseCallback<Boolean>) {
        viewModelScope.launch {
            repository.addNewGroupToFirebase(data).catch {
                callback.isFailed(it.message.toString())
            }.collect { isAdded ->
                callback.isSuccess(isAdded)
            }
        }
    }
    fun deleteGroupFromFirebase(data: GroupDetailData, callback: FirebaseCallback<Boolean>) {
        viewModelScope.launch {
            repository.deleteGroupFromFirebase(data).catch {
                callback.isFailed(it.message.toString())
            }.collect {
                callback.isSuccess(it)
            }
        }
    }
}