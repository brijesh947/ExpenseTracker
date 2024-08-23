package com.example.splitwise.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.data.CategoryDescriptionData
import com.example.splitwise.data.Data
import com.example.splitwise.data.RecentTransactionData
import com.example.splitwise.data.SimpleTextData
import com.example.splitwise.databinding.CategoryFragmentLayoutBinding
import com.example.splitwise.ui.CategoryFragmentAdapter
import com.example.splitwise.ui.util.SIMPLE_TEXT
import java.util.Calendar

class CategoryFragment : BaseFragment() {

    lateinit  var recyclerView: RecyclerView
    private lateinit var binding: CategoryFragmentLayoutBinding

    private val list :ArrayList<Data> = ArrayList()

    private val adapter : CategoryFragmentAdapter = CategoryFragmentAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CategoryFragmentLayoutBinding.inflate(layoutInflater)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        setCurrentMonth()
        val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        binding.monthFilter.setOnClickListener {
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(50)
            }
            Toast.makeText(requireContext(), "It is always set to current Month", Toast.LENGTH_SHORT).show()
        }

        prepareData()
        return binding.root
    }

    private var currMonth = -1
    private var currYear = -1

    private fun setCurrentMonth() {
        currMonth = MonthManager.getInstance().getCurrentMonth()
        currYear = MonthManager.getInstance().getCurrentYear()
        binding.userGroupName.text = getMonthName(currMonth) + " " + currYear
    }

    private fun prepareData() {
        list.clear()
        list.add(SimpleTextData("Default monthly budget is set at ₹50000,with ₹10000 allocated for individual categories.\nYou can change it from budget tab"))
        list.add(RecentTransactionData("Expense categories"))

        for (index in 101..113) {
            list.add(CategoryDescriptionData(index))
        }
        list.add(SimpleTextData("\nCreate new category feature will be available soon"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onResume() {
        super.onResume()
        adapter.setList(list)
    }

}