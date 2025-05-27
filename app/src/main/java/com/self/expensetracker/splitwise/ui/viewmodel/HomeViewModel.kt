package com.self.expensetracker.splitwise.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.self.expensetracker.splitwise.FirebaseCallback
import com.self.expensetracker.splitwise.data.Data
import com.self.expensetracker.splitwise.data.GroupDetailData
import com.self.expensetracker.splitwise.data.ShoppingData
import com.self.expensetracker.splitwise.data.UserPersonalData
import com.self.expensetracker.splitwise.ui.repository.HomeRepository
import com.self.expensetracker.splitwise.ui.util.CategoryManager
import com.self.expensetracker.splitwise.ui.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private var _groupDetail = MutableStateFlow<UiState<List<GroupDetailData>>>(UiState.Loading)
    private var _expenseDetail = MutableStateFlow<UiState<List<Data>>>(UiState.Loading)

    private var _userPersonalDetail = MutableStateFlow<UiState<UserPersonalData>>(UiState.Loading)
    val userPersonalDetail: StateFlow<UiState<UserPersonalData>> = _userPersonalDetail


    val expenseDetail :StateFlow<UiState<List<Data>>> get() = _expenseDetail
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

    fun getCategoryDetail(groupDetailData: GroupDetailData) {
        viewModelScope.launch {
            repository.getTotalCategoryType(groupDetailData).catch {
                //do  nothing
                Log.d("jbkdsasvlh", "exception while fetching category ${it.message}")
            }.collect {
                //do nothing
            }
        }
    }

    fun createNewCategory(groupDetailData: GroupDetailData, categoryData: CategoryManager.CategoryHolderData, callback: FirebaseCallback<Boolean>) {
        viewModelScope.launch {
            repository.addNewCategoryType(groupDetailData, categoryData).catch {
                //do  nothing
                Log.d("jbkdsasvlh", "exception while fetching category ${it.message}")
                callback.isFailed(it.message.toString())
            }.collect {
                //do nothing
                callback.isSuccess(it)
            }
        }
    }

    fun getUserPersonalDetail(callback: FirebaseCallback<List<String>>) {
        viewModelScope.launch {
            repository.getUserPersonalDetail().catch {
                callback.isFailed(it.message!!)
            }.collect {
                if (it.isEmpty())
                    callback.isFailed("List is Empty")
                else
                    callback.isSuccess(it)
            }
        }
    }

    fun getUserPersonalDetail() {
        _userPersonalDetail.value = UiState.Loading
        viewModelScope.launch {
            repository.getUserPersonalDetail().catch {
                _userPersonalDetail.value =
                    UiState.Error(it?.message ?: "Something went wrong")
            }.collect {
                if (it.isEmpty())
                    UiState.Error("Response is Empty")
                else {
                    val userPersonalData = UserPersonalData(it[0], it[1], it[0][0].toString())
                    _userPersonalDetail.value = UiState.Success(userPersonalData)
                }
            }
        }
    }


    fun updateNameAndPassword(
        name: String,
        updatedPassword: String,
        callback: FirebaseCallback<String>
    ) {

        viewModelScope.launch {
            val result = repository.updateUserNameAndPassword(name, updatedPassword)
            if (result.isSuccess) {
                callback.isSuccess(result = result.toString())
            } else {
                callback.isFailed(reason = result.toString())
            }
        }
    }

    fun getExpenseDetail(groupDetailData: GroupDetailData, currMonth: Int, currYear: Int) {
        viewModelScope.launch {
            Log.d("TAGD", "getUserDetail in view model  is called")
            _expenseDetail.value = UiState.Loading
            repository.getUserExpenseDetail(groupDetailData,currMonth,currYear).catch { e ->
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



    fun addNewUserGroup(data: GroupDetailData, callback: FirebaseCallback<Boolean>) {
        viewModelScope.launch {
            repository.addNewGroupToFirebase(data).catch {
                callback.isFailed(it.message.toString())
            }.collect { isAdded ->
                callback.isSuccess(isAdded)
            }
        }
    }

    fun addNewUserExpenses(data: GroupDetailData,shoppingData: ShoppingData, callback: FirebaseCallback<Boolean>) {
        viewModelScope.launch {
            repository.addNewExpenseToFirebase(data,shoppingData).catch {
                callback.isFailed(it.message.toString())
            }.collect { isAdded ->
                callback.isSuccess(isAdded)
            }
        }
    }

    fun updateUserExpenses(data: GroupDetailData, shoppingData: ShoppingData, callback: FirebaseCallback<Boolean>) {
        viewModelScope.launch {
            repository.updateExpenseToFirebase(data, shoppingData).catch {
                callback.isFailed(it.message.toString())
            }.collect { isAdded ->
                callback.isSuccess(isAdded)
            }
        }
    }

    fun deleteUserExpenses(data: GroupDetailData,shoppingData: ShoppingData, callback: FirebaseCallback<Boolean>) {
        viewModelScope.launch {
            repository.deleteExpenseToFirebase(data,shoppingData).catch {
                callback.isFailed(it.message.toString())
            }.collect { isAdded ->
                callback.isSuccess(isAdded)
            }
        }
    }

    fun updateTotalExpense(data: GroupDetailData, totalExpense: String) {
        Log.d("fghgjkhgdt", "updateTotalExpense: is called $totalExpense")
        viewModelScope.launch {
            repository.updateTotalExpensesInGroup(data, totalExpenses = totalExpense)
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