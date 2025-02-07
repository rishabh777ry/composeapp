package com.example.composeapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.composeapp.ui.screens.Login
import com.example.composeapp.ui.theme.ComposeAppTheme
import com.example.composeapp.viewmodel.UserViewModel


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel :AuthViewModel by viewModels()
        val viewModel: UserViewModel by viewModels()

        setContent {
            ComposeAppTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = Color.White
                    ) {
                        Box{
                         navGraph(authViewModel,viewModel)
                        }
                        }

                }
            }
        }
    }






