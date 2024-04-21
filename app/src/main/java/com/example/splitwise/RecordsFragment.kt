package com.example.splitwise

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.data.ShoppingData
import com.example.splitwise.databinding.AddExpenseLayoutBinding
import com.example.splitwise.databinding.RecordFragmentLayoutBinding
import com.example.splitwise.ui.HomeAdapter
import com.example.splitwise.ui.HomeViewModel
import com.example.splitwise.ui.di.component.DaggerExpenseDetailActivityComponent
import com.example.splitwise.ui.di.module.ExpenseDetailActivityModule
import com.example.splitwise.ui.di.module.HomeActivityModule
import com.example.splitwise.ui.util.UiState
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecordsFragment(val application: MyApplication,val activity: ExpenseDetailActivity) : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: RecordFragmentLayoutBinding
    private var  list :ArrayList<ShoppingData> = ArrayList()
    private var groupData:GroupDetailData? = null

    @Inject
    lateinit var adapter: HomeAdapter

    @Inject
    lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        initFragment()
        super.onCreate(savedInstanceState)
    }

    fun setGroupData(data: GroupDetailData?) {
        groupData = data
    }

    private fun initFragment() {
        DaggerExpenseDetailActivityComponent.builder()
            .applicationComponent((application as MyApplication).applicationComponent)
            .homeActivityModule(HomeActivityModule(activity = activity as AppCompatActivity))
            .expenseDetailActivityModule(ExpenseDetailActivityModule(application,activity))
            .build()
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = RecordFragmentLayoutBinding.inflate(layoutInflater)
        binding.addRecordsButton.setOnClickListener {
            openCreateGroupDialog()
        }

        recyclerView = binding.expenseRecylerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter.setViewType(2)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun openCreateGroupDialog() {
        val dialogView = AddExpenseLayoutBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialogView.expenseFilterParent.groupType1.text = "Rent"
        dialogView.expenseFilterParent.groupType2.text = "Health"
        dialogView.expenseFilterParent.groupType3.text = "Food"
        dialogView.createGroupButton.setOnClickListener {
            if(verifyInput(dialogView)){
                binding.noElement.visibility = View.GONE
                var filter = ""
                filter = if (dialogView.expenseFilterParent.groupType1.isChecked) {
                    "Rent"
                } else if (dialogView.expenseFilterParent.groupType2.isChecked) {
                    "Health"
                } else if (dialogView.expenseFilterParent.groupType3.isChecked) {
                    "Food"
                } else {
                    "Other"
                }
                val data = ShoppingData("",dialogView.shoppingName.text.toString(),filter,dialogView.shoppingPrice.text.toString())
                totalShoppingSum += data.totalAmount.toDouble()
                binding.totalExpense.text = totalShoppingSum.toString()
                binding.totalExpense.setTextColor(requireActivity().resources.getColor(R.color.ce_highlight_khayi_light))
                viewModel.addNewUserExpenses(groupData!!, data, object : FirebaseCallback<Boolean> {
                    override fun isSuccess(result: Boolean) {
                        if(result){
                            list.add(data)
                            adapter.setList(list)
                        }
                    }

                    override fun isFailed(reason: String) {
                        Log.d("kflwh", "Failed to add expenses $reason")
                    }

                })
                viewModel.updateTotalExpense(groupData!!,totalShoppingSum.toString())
                dialog.dismiss()
            }
        }
        dialog.setContentView(dialogView.root)
        dialog.show()
    }

    private fun verifyInput(dialogView: AddExpenseLayoutBinding): Boolean {
        if (dialogView.shoppingName.text.isEmpty()) {
            dialogView.shoppingName.error = "Shopping name can't be Empty"
            return false
        }
        if (dialogView.shoppingPrice.text.isEmpty()) {
            dialogView.shoppingPrice.error = "Shopping price can't be Empty"
            return false
        }

        return true
    }

    private var totalShoppingSum = 0.0

    override fun onResume() {
        super.onResume()
        totalShoppingSum = 0.0
        adapter.setViewType(2)
        fetchData()
    }

    @SuppressLint("RepeatOnLifecycleWrongUsage")
    private fun fetchData() {
        viewModel.getExpenseDetail(groupData!!)
        groupData?.let {
            binding.userGroupName.text = it.groupName
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.expenseDetail.collect {
                    when (it) {
                        is UiState.Loading -> {
                            list = ArrayList()
                            adapter.setList(list)
                            binding.totalExpense.text = "No Expenses"
                            binding.totalExpense.setTextColor(requireActivity().resources.getColor(R.color.secondary_txt))
                            binding.progressBar.visibility = View.VISIBLE
                            binding.noElement.visibility = View.GONE
                        }

                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            if (it.data.isNotEmpty()) {
                                list = it.data as ArrayList<ShoppingData>
                                adapter.setList(list)
                                totalShoppingSum = 0.0
                                list.forEach { shoppingData ->
                                    totalShoppingSum += shoppingData.totalAmount.toDouble()
                                }
                                binding.totalExpense.text = totalShoppingSum.toString()
                                binding.totalExpense.setTextColor(requireActivity().resources.getColor(R.color.ce_highlight_khayi_light))
                                binding.noElement.visibility = View.GONE
                            } else {
                                binding.noElement.visibility = View.VISIBLE
                            }

                            viewModel.updateTotalExpense(groupData!!,totalShoppingSum.toString())

                        }

                        else -> {
                            binding.progressBar.visibility = View.GONE
                            binding.noElement.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

}