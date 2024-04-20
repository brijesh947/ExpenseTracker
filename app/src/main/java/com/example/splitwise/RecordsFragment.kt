package com.example.splitwise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.data.ShoppingData
import com.example.splitwise.databinding.AddExpenseLayoutBinding
import com.example.splitwise.databinding.RecordFragmentLayoutBinding
import com.example.splitwise.ui.HomeAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class RecordsFragment : Fragment() {
    private lateinit var adapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: RecordFragmentLayoutBinding
    private val  list :ArrayList<ShoppingData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = RecordFragmentLayoutBinding.inflate(layoutInflater)
        binding.addRecordsButton.setOnClickListener {
            openCreateGroupDialog()
        }

        recyclerView = binding.expenseRecylerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = HomeAdapter(requireContext(),requireContext().applicationContext as MyApplication)
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
                val data = ShoppingData(dialogView.shoppingName.text.toString(),filter,dialogView.shoppingPrice.text.toString())
                totalShoppingSum += data.totalAmount.toDouble()
                binding.totalExpense.text = totalShoppingSum.toString()
                binding.totalExpense.setTextColor(requireActivity().resources.getColor(R.color.ce_highlight_khayi_light))
                list.add(data)
                adapter.setList(list)
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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }
    private var totalShoppingSum = 0.0

    override fun onResume() {
        super.onResume()
        totalShoppingSum = 0.0
        adapter.setViewType(2)
        if (list.isNotEmpty()) {
            binding.noElement.visibility = View.GONE
            list.forEach {
                totalShoppingSum+= it.totalAmount.toDouble()
            }
            binding.totalExpense.text = totalShoppingSum.toString()
            binding.totalExpense.setTextColor(requireActivity().resources.getColor(R.color.ce_highlight_khayi_light))

            adapter.setList(list)
        } else {
            binding.totalExpense.text = "No Expenses to Show"
            binding.totalExpense.setTextColor(requireActivity().resources.getColor(R.color.secondary_txt))
            binding.noElement.visibility = View.VISIBLE
        }
    }

}