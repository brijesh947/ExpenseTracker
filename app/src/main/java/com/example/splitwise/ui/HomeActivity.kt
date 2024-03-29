package com.example.splitwise.ui

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.R
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.databinding.CreateGroupBinding
import com.example.splitwise.databinding.HomeLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {
    private var db: FirebaseFirestore?=null
    private lateinit var binding: HomeLayoutBinding
    private var list = ArrayList<GroupDetailData>()
    private lateinit var recyclerView :RecyclerView
    private lateinit var adapter: HomeAdapter
    private lateinit var auth: FirebaseAuth
    private  var user: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowColor()
        Log.d("Brijesh", "in Home Activity")
        binding.createGroup.setOnClickListener {
            openCreateGroupDialog()
        }
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        db = FirebaseFirestore.getInstance()
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        adapter = HomeAdapter()
        recyclerView.adapter = adapter

    }


    override fun onResume() {
        fetchFirebaseData()
        super.onResume()
    }

    private fun fetchFirebaseData() {
        if (user != null) {
            val userRef = db!!.collection("users").document(user!!.uid).collection("userDetail")
            userRef.get().addOnSuccessListener {
                if (it != null && !it.isEmpty) {

                    for (doc in it.documents) {
                        var tempData = GroupDetailData("" + doc.get("group_name"), "Home", "" + doc.get("total_expense"))
                        list.add(tempData)
                    }
                    adapter.setList(list)
                }
            }.addOnFailureListener {
                Log.d("TAGD", "exception while reading the firestore data ${it.message}")
            }
        }else{
            Log.d("TAGD", "user is null from firestore")
        }
    }

    private fun openCreateGroupDialog() {
        val dialogView = CreateGroupBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(this)
        dialogView.createGroupButton.setOnClickListener {
             if(verifyInput(dialogView)){
                 val data = GroupDetailData(dialogView.groupName.text.toString().trim(),"Home","50000")
                 addTotheFirebase(data)
                 list.add(data)
                 adapter.setList(list)
                 dialog.dismiss()
             }
        }
        dialog.setContentView(dialogView.root)
        dialog.show()
    }

    private fun addTotheFirebase(data: GroupDetailData) {

        if(user!=null){
            val userRef = db!!.collection("users").document(user!!.uid).collection("userDetail")
            val userDetail = hashMapOf(
                "group_name" to data.groupName,
                "total_expense" to data.totalExpense
            )
            userRef.add(userDetail).addOnSuccessListener {
                Log.d("TAGD", "data set to the firestore")
            }.addOnFailureListener {
                Log.d("TAGD", "failed due to ${it.message}")
            }
        }
    }

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