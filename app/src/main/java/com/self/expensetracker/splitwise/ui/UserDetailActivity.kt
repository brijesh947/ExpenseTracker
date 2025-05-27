package com.self.expensetracker.splitwise.ui

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

import com.self.expensetracker.splitwise.FirebaseCallback
import com.self.expensetracker.splitwise.MyApplication
import com.self.expensetracker.splitwise.R
import com.self.expensetracker.splitwise.data.UserPersonalData
import com.self.expensetracker.splitwise.ui.compose.UserDetailScreen
import com.self.expensetracker.splitwise.ui.di.component.DaggerUserDetailComponent
import com.self.expensetracker.splitwise.ui.di.module.UserDetailActivityModule
import com.self.expensetracker.splitwise.ui.util.UiState
import com.self.expensetracker.splitwise.ui.viewmodel.HomeViewModel
import javax.inject.Inject

class UserDetailActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            userDetailScreenFull()
        }

    }

    private fun injectDependencies() {
        DaggerUserDetailComponent.builder()
            .applicationComponent((application as MyApplication).applicationComponent)
            .userDetailActivityModule(UserDetailActivityModule(this)).build().inject(this)
    }



    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun userDetailScreenFull(modifier: Modifier = Modifier) {

        val userPersonalData by viewModel.userPersonalDetail.collectAsState()

        val context = LocalContext.current
        val activity = context as? Activity

        LaunchedEffect(Unit) {
            viewModel.getUserPersonalDetail()
        }

        // Toast message state
        var toastMessage by remember { mutableStateOf<String?>(null) }

        // Show toast when message is updated
        LaunchedEffect(toastMessage) {
            toastMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                toastMessage = null // Reset after showing
            }
        }


        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(R.color.primary_bg))
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 24.dp,
                                    bottomEnd = 24.dp
                                )
                            )
                            .background(colorResource(R.color.app_bar_background))

                    ) {
                        TopAppBar(
                            title = {
                                Text(text = "Edit Profile")
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    activity?.finish() // Go back to parent activity
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent // Let Box background show through
                            )
                        )
                    }


                }

            }
        ) {

            if (userPersonalData is UiState.Loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (userPersonalData is UiState.Success) {
                val personalData = (userPersonalData as UiState.Success<UserPersonalData>).data
                var firstName by remember { mutableStateOf(personalData.firstName) }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(it)
                        .background(colorResource(R.color.primary_bg))
                ) {

                    UserDetailScreen(
                        personalData.name,
                        personalData.email,
                        firstName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        onUpdateClick = { newName, newEmail, newPassword ->

                            if (newName == personalData.name) {
                                toastMessage = "No update found"
                                return@UserDetailScreen
                            }

                            if (newName.isBlank() || newName.isEmpty()) {
                                toastMessage = "Name Can't be empty"
                                return@UserDetailScreen
                            }

                            viewModel.updateNameAndPassword(newName, newPassword, object : FirebaseCallback<String> {
                                    override fun isSuccess(result: String) {
                                        toastMessage = "Name $newName updated successfully"
                                        firstName = newName[0].toString()
                                    }

                                    override fun isFailed(reason: String) {
                                        toastMessage = "Name $newName updation failed due to $reason"
                                    }
                                })
                        }
                    )
                }
            }


        }


    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}