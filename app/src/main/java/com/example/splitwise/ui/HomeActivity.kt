package com.example.splitwise.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.MyApplication
import com.example.splitwise.R
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.databinding.CreateGroupBinding
import com.example.splitwise.databinding.HomeLayoutBinding
import com.example.splitwise.ui.di.component.DaggerHomeActivityComponent
import com.example.splitwise.ui.di.module.HomeActivityModule
import com.example.splitwise.ui.util.UiState
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeLayoutBinding
    private var list = ArrayList<GroupDetailData>()
    private lateinit var recyclerView :RecyclerView
    private lateinit var adapter: HomeAdapter

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        binding = HomeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowColor()
        Log.d("Brijesh", "in Home Activity")
        binding.createGroup.setOnClickListener {
            openCreateGroupDialog()
        }
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        adapter = HomeAdapter()
        recyclerView.adapter = adapter

    }

    private fun injectDependencies() {
        DaggerHomeActivityComponent.builder()
            .applicationComponent((application as MyApplication).applicationComponent)
            .homeActivityModule(HomeActivityModule(this)).build().inject(this)
    }


    override fun onResume() {
        super.onResume()
        fetchData()
    }

    @SuppressLint("RepeatOnLifecycleWrongUsage")
    private fun fetchData() {
        viewModel.getUserDetail()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.groupDetail.collect{
                    when(it){
                        is UiState.Success ->{
                            if (it.data.isNotEmpty())
                                adapter.setList(it.data)
                            else
                                Toast.makeText(this@HomeActivity,"List is Empty", Toast.LENGTH_SHORT).show()
                        }
                        else ->{
                            Toast.makeText(this@HomeActivity,"Error from firebase $it", Toast.LENGTH_SHORT).show()
                        }

                    }
                }

            }
        }

    }


    private fun openCreateGroupDialog() {
        val dialogView = CreateGroupBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(this)
        dialogView.createGroupButton.setOnClickListener {
             if(verifyInput(dialogView)){
                 val data = GroupDetailData(dialogView.groupName.text.toString().trim(),"Home","50000")
               //  addTotheFirebase(data)
                 list.add(data)
                 adapter.setList(list)
                 dialog.dismiss()
             }
        }
        dialog.setContentView(dialogView.root)
        dialog.show()
    }

//    private fun addTotheFirebase(data: GroupDetailData) {
//        if(user!=null){
//            val userRef = db!!.collection("users").document(user!!.uid).collection("userDetail")
//            val userDetail = hashMapOf(
//                "group_name" to data.groupName,
//                "total_expense" to data.totalExpense
//            )
//            userRef.add(userDetail).addOnSuccessListener {
//                Log.d("TAGD", "data set to the firestore")
//            }.addOnFailureListener {
//                Log.d("TAGD", "failed due to ${it.message}")
//            }
//        }
//    }

    private fun verifyInput(dialogView: CreateGroupBinding): Boolean {

        if (dialogView.groupName.text.isEmpty()) {
            dialogView.groupName.error = "Group name can't be Empty"
            return false
        }
        return true
    }

    override fun onPause() {
        super.onPause()
    }

    private fun setWindowColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(R.color.transparent)
    }
}