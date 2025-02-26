package com.self.expensetracker.splitwise.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextPaint
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.self.expensetracker.splitwise.FirebaseCallback
import com.self.expensetracker.splitwise.MyApplication
import com.self.expensetracker.splitwise.R
import com.self.expensetracker.splitwise.data.GroupDetailData
import com.self.expensetracker.splitwise.databinding.CreateGroupBinding
import com.self.expensetracker.splitwise.databinding.DeleteGroupAlertDialogBinding
import com.self.expensetracker.splitwise.databinding.HomeLayoutBinding
import com.self.expensetracker.splitwise.ui.di.component.DaggerHomeActivityComponent
import com.self.expensetracker.splitwise.ui.di.module.HomeActivityModule
import com.self.expensetracker.splitwise.ui.util.UiState
import com.self.expensetracker.splitwise.ui.util.hide
import com.self.expensetracker.splitwise.ui.util.show
import com.self.expensetracker.splitwise.ui.viewmodel.HomeViewModel
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
        setUserNameAndEmail()
        recyclerView.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        setScrollListener()

    }

    private fun setScrollListener() {

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private val HIDE_THRESHOLD = 20
            private var scrolledDistance = 0
            private var controlsVisible = true
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    // Hide the floating button with animation
                    binding.createGroup.animate().translationY((binding.createGroup.height + 110).toFloat())
                        .setInterpolator(AccelerateInterpolator())
                        .setDuration(300).start()
                    controlsVisible = false
                    scrolledDistance = 0
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    // Show the floating button with animation
                    binding.createGroup.animate().translationY(0F)
                        .setInterpolator(DecelerateInterpolator())
                        .setDuration(300).start()
                    controlsVisible = true
                    scrolledDistance = 0
                }
                if (controlsVisible && dy > 0 || !controlsVisible && dy < 0) {
                    scrolledDistance += dy
                }
            }
        })
    }

    private fun setUserNameAndEmail() {
        val pref = this.getSharedPreferences("user_detail", Context.MODE_PRIVATE)
        viewModel.getUserPersonalDetail(object : FirebaseCallback<List<String>> {

            override fun isSuccess(result: List<String>) {
                if (result.isNotEmpty()) {
                    binding.userName.text = result[0]
                    binding.userEmail.text = result[1]
                    binding.userImage.text = result[0][0].toString()
                }
            }

            override fun isFailed(reason: String) {
                binding.userName.text = pref.getString("name", "User")
                binding.userEmail.text = pref.getString("email", "No email found")
            }
        })
    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            openAlertDialog(position)
        }

        override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            if ( actionState != ItemTouchHelper.ACTION_STATE_SWIPE ) return
            if (dX > 0) {

                val radius = 17f * resources.displayMetrics.density // Set the corner radius here

                val clipPath = Path().apply {
                    addRoundRect(
                        RectF(
                            viewHolder.itemView.left.toFloat(),
                            viewHolder.itemView.top.toFloat(),
                            viewHolder.itemView.left.toFloat() + dX,
                            viewHolder.itemView.bottom.toFloat()
                        ),
                        radius,
                        radius,
                        Path.Direction.CW
                    )
                }

                canvas.clipPath(clipPath)

                val background = ColorDrawable(resources.getColor(R.color.pausedColor))
                background.setBounds(
                    viewHolder.itemView.left,
                    viewHolder.itemView.top,
                    viewHolder.itemView.left + dX.toInt(),
                    viewHolder.itemView.bottom
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
                val radius = 17f * resources.displayMetrics.density // Set the corner radius here

                val clipPath = Path().apply {
                    addRoundRect(
                        RectF(
                            (viewHolder.itemView.right + dX.toInt()).toFloat(),
                            viewHolder.itemView.top.toFloat(),
                            viewHolder.itemView.right.toFloat(),
                            viewHolder.itemView.bottom.toFloat()
                        ),
                        radius,
                        radius,
                        Path.Direction.CW
                    )
                }

                canvas.clipPath(clipPath)

                val background = ColorDrawable(resources.getColor(R.color.pausedColor))
                background.setBounds(
                    viewHolder.itemView.right + dX.toInt(),
                    viewHolder.itemView.top,
                    viewHolder.itemView.right,
                    viewHolder.itemView.bottom
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

    private fun openAlertDialog(position: Int) {
        val view = DeleteGroupAlertDialogBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(this).setView(view.root)
        val dialog = builder.create()

        dialog.setCancelable(false)

        view.positiveButton.setOnClickListener {
            adapter.notifyItemChanged(position)
            dialog.dismiss()
        }

        view.negativeButton.setOnClickListener {

            viewModel.deleteGroupFromFirebase(list[position], object : FirebaseCallback<Boolean> {
                override fun isSuccess(result: Boolean) {
                    Log.d("bghjgkj", "delete item is success $result")
                    if (result) {
                        list.removeAt(position)
                        adapter.notifyItemRemoved(position)
                    } else
                        showError("Error while deleting the data")
                    if (list.isEmpty()) {
                        binding.noGroup.show()
                    }
                }

                override fun isFailed(reason: String) {
                    showError(reason)
                }

            })
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun injectDependencies() {
        DaggerHomeActivityComponent.builder()
            .applicationComponent((application as MyApplication).applicationComponent)
            .homeActivityModule(HomeActivityModule(this)).build().inject(this)
    }


    override fun onResume() {
        super.onResume()
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
                            binding.progressBar.hide()
                            if (it.data.isNotEmpty()) {
                                list = it.data as ArrayList<GroupDetailData>
                                adapter.setList(list)
                                binding.noGroup.hide()
                            } else {
                                binding.noGroup.show()
                            }

                        }

                        is UiState.Loading -> {
                            binding.progressBar.show()
                        }

                        else -> {
                            binding.progressBar.hide()
                            binding.noGroup.show()
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

                 var group = ""
                 group = if (dialogView.createGroupParent.groupType1.isChecked)
                     "Flatmates"
                 else if (dialogView.createGroupParent.groupType2.isChecked)
                     "Home"
                 else if (dialogView.createGroupParent.groupType3.isChecked)
                     "Couple"
                 else
                     "Other"


                 val data = GroupDetailData("0",dialogView.groupName.text.toString().trim(),group,"Add Expense to display here.")
                 viewModel.addNewUserGroup(data, object : FirebaseCallback<Boolean> {
                     override fun isSuccess(result: Boolean) {
                         if (result) {
                             list.add(data)
                             adapter.setList(list)
                         } else
                             showError("Unable to create group")
                         if (list.isNotEmpty()) {
                             binding.noGroup.hide()
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
        window.statusBarColor = resources.getColor(R.color.app_bar_background)
    }
}