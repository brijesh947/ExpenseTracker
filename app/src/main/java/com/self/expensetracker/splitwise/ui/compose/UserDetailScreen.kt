package com.self.expensetracker.splitwise.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.self.expensetracker.splitwise.R


@Composable
fun UserDetailScreen(
    name1: String,
    email: String,
    firstLetterOfName: String,
    modifier: Modifier = Modifier,
    onUpdateClick: (String, String, String) -> Unit
) {
    Column(
        modifier,
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var name by rememberSaveable { mutableStateOf(name1) }
        var email by remember { mutableStateOf(email) }
        var password by remember { mutableStateOf("") }

        Spacer(Modifier.padding(top = 32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            profileImage(firstLetterOfName.toUpperCase())
        }

        Spacer(Modifier.padding(top = 32.dp))

        HeadingText("Name*")
        Spacer(Modifier.padding(2.dp))
        ShoppingNameInputField(name, onTextChange = {
            name = it
        })

        Spacer(Modifier.padding(12.dp))

        HeadingText("Email*")
        Spacer(Modifier.padding(2.dp))
        ShoppingNameInputField(email,null,false, onTextChange = {
            email = it
        })

        Spacer(Modifier.padding(12.dp))

//        HeadingText("Password")
//        Spacer(Modifier.padding(2.dp))
//
//        ShoppingNameInputField(password,null,false, onTextChange = {
//            password = it
//        })

        Spacer(modifier = Modifier.padding(16.dp))
        ActionButton("UPDATE",{
            onUpdateClick(name, email, password)
        })
    }

}

@Composable
fun ShoppingNameInputField(
    text: String,
    placeHolder: String? = null,
    focusable:Boolean = true,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = text,
        onValueChange = {
            onTextChange(it)
        },
        enabled = focusable,
        readOnly = !focusable,
        placeholder = {
            Text(text = placeHolder?.let { it } ?: "Enter the Detail",
                modifier = Modifier.alpha(.5f))
        },
        textStyle = TextStyle(
            fontSize = 12.sp,
            color = colorResource(R.color.primary_txt),
            fontFamily = FontFamily(Font(R.font.abc_diatype_regular)), // Make sure to import your font
            textMotion = TextMotion.Animated
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(R.color.progress_bar_background), // Customize as needed
            unfocusedBorderColor = colorResource(R.color.progress_bar_background), // Customize as needed
            cursorColor = Color.Black // Customize as needed
        ),
        shape = RoundedCornerShape(16.dp), // 👈 Set corner radius here
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 2.dp)
            .alpha(if(focusable) 1f else 0.5f),

    )


}

@Composable
fun profileImage(firstLetterOfName: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(RoundedCornerShape(42.dp))
            .background(Color(android.graphics.Color.parseColor("#cffebe69"))),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = firstLetterOfName,
            color = colorResource(R.color.ce_high_contrast_txt_color_dark),
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.abc_diatype_bold))
        )
    }
}


@Composable
fun HeadingText(text: String, modifier: Modifier = Modifier) {

    Text(
        text = text,
        color = colorResource(id = R.color.primary_txt),
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.abc_diatype_medium)),
        modifier = Modifier
            .padding(start = 12.dp)
            .wrapContentSize()
    )
}


