package com.example.splitwise.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextPaint
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splitwise.FirebaseCallback
import com.example.splitwise.MyApplication
import com.example.splitwise.R
import com.example.splitwise.data.GroupDetailData
import com.example.splitwise.databinding.CreateGroupBinding
import com.example.splitwise.databinding.HomeLayoutBinding
import com.example.splitwise.ui.di.component.DaggerHomeActivityComponent
import com.example.splitwise.ui.di.module.HomeActivityModule
import com.example.splitwise.ui.util.UiState
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

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
        adapter = HomeAdapter(this,application)
        adapter.setViewType(1)
        setUserNameAndEmail()
        recyclerView.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun setUserNameAndEmail() {
        val pref = this.getSharedPreferences("user_detail",Context.MODE_PRIVATE)
        binding.userName.text =  pref.getString("name","NA")
        binding.userEmail.text =  pref.getString("email","NA")
    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            viewModel.deleteGroupFromFirebase(list[position], object : FirebaseCallback<Boolean> {
                    override fun isSuccess(result: Boolean) {
                        Log.d("bghjgkj", "delete item is success $result")
                        if (result) {
                            list.removeAt(position)
                            adapter.notifyItemRemoved(position)
                        } else
                            showError("Error while deleting the data")
                        if (list.isEmpty()) {
                            binding.noGroup.visibility = View.VISIBLE
                        }
                    }

                    override fun isFailed(reason: String) {
                        showError(reason)
                    }

                })
        }

        override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            if ( actionState != ItemTouchHelper.ACTION_STATE_SWIPE ) return
            if (dX > 0) {
                canvas.clipRect(viewHolder.itemView.left, viewHolder.itemView.top, viewHolder.itemView.left + dX.toInt(), viewHolder.itemView.bottom)
                val background = ColorDrawable(resources.getColor(R.color.text_cta_color))
                background.setBounds(
                    viewHolder.itemView.left,
                    viewHolder.itemView.top + (20 * (resources.displayMetrics.density.toInt())),
                    viewHolder.itemView.left + dX.toInt(),
                    viewHolder.itemView.bottom - (20 * (resources.displayMetrics.density.toInt()))
                )
                background.draw(canvas)

                val textPaint = TextPaint()
                textPaint.isAntiAlias = true
                textPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20f, recyclerView.context.resources.displayMetrics)
                textPaint.color = getColor(R.color.white)
                textPaint.typeface = Typeface.DEFAULT_BOLD
                val textTop = (viewHolder.itemView.top + (viewHolder.itemView.bottom - viewHolder.itemView.top) / 2.0 + textPaint.textSize / 2).toInt()
                canvas.drawText("Delete", viewHolder.itemView.left + (23 * (resources.displayMetrics.density.toInt())).toFloat() , (textTop-2).toFloat(), textPaint)

            } else {
                canvas.clipRect(
                    viewHolder.itemView.right + dX.toInt(),
                    viewHolder.itemView.top,
                    viewHolder.itemView.right,
                    viewHolder.itemView.bottom
                )
                val background = ColorDrawable(resources.getColor(R.color.text_cta_color))
                background.setBounds(
                    viewHolder.itemView.right + dX.toInt(),
                    viewHolder.itemView.top + (20 * (resources.displayMetrics.density.toInt())),
                    viewHolder.itemView.right,
                    viewHolder.itemView.bottom - (20 * (resources.displayMetrics.density.toInt()))
                )

                background.draw(canvas)
                val textPaint = TextPaint()
                textPaint.isAntiAlias = true
                textPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20f, recyclerView.context.resources.displayMetrics)
                textPaint.color = getColor(R.color.white)
                textPaint.typeface = Typeface.DEFAULT_BOLD
                val textTop = (viewHolder.itemView.top + (viewHolder.itemView.bottom - viewHolder.itemView.top) / 2.0 + textPaint.textSize / 2).toInt()
                canvas.drawText("Delete", viewHolder.itemView.right + dX.toFloat() + 100f , (textTop).toFloat(), textPaint)
            }
            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }



    private fun injectDependencies() {
        DaggerHomeActivityComponent.builder()
            .applicationComponent((application as MyApplication).applicationComponent)
            .homeActivityModule(HomeActivityModule(this)).build().inject(this)
    }


    override fun onResume() {
        super.onResume()
        adapter.setViewType(1)
        fetchData()
        val appBarLayout1: AppBarLayout = binding.appBar
        appBarLayout1.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val percent = abs(verticalOffset) * 1f / (appBarLayout1.totalScrollRange * 1f)
            binding.userDetailParent.alpha = 1 - percent
        }
    }

    @SuppressLint("RepeatOnLifecycleWrongUsage")
    private fun fetchData() {
        viewModel.getUserDetail()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.groupDetail.collect{
                    when(it){
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            if (it.data.isNotEmpty()) {
                                list = it.data as ArrayList<GroupDetailData>
                                adapter.setList(list)
                                binding.noGroup.visibility = View.GONE
                            } else {
                                binding.noGroup.visibility = View.VISIBLE
                            }

                        }

                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        else -> {
                            binding.progressBar.visibility = View.GONE
                            binding.noGroup.visibility = View.VISIBLE
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
                 val data = GroupDetailData("0",dialogView.groupName.text.toString().trim(),"Home","till now No Expenses")
                 viewModel.addNewUserGroup(data, object : FirebaseCallback<Boolean> {
                     override fun isSuccess(result: Boolean) {
                         if (result) {
                             list.add(data)
                             adapter.setList(list)
                         } else
                             showError("User not Added")
                         if (list.isNotEmpty()) {
                             binding.noGroup.visibility = View.GONE
                         }
                     }
                     override fun isFailed(reason: String) {
                         showError(reason)
                     }
                 })

                 dialog.dismiss()
             }
        }
        dialog.setContentView(dialogView.root)
        dialog.show()
    }

    private fun showError(reason: String) {
        Log.d("TAGD", "error whiled adding data to the firebase is $reason")
        Toast.makeText(this@HomeActivity,reason,Toast.LENGTH_SHORT).show()
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