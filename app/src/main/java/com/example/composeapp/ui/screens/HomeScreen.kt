package com.example.composeapp.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeapp.viewmodel.UserViewModel


@Composable
fun HomeScreen(viewModel: UserViewModel){

    val user = viewModel.getUser()
    Box(Modifier.padding(50.dp)){
        Text(
            "Home Screen",
            modifier = Modifier.clickable {
                Log.d(">>//"," user details $user")
            },
            fontSize = 35.sp,
            color = Color.Red
        )
    }
}