package com.example.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeapp.ui.screens.HomeScreen
import com.example.composeapp.ui.screens.Login
import com.example.composeapp.ui.screens.SignUP
import com.example.composeapp.viewmodel.UserViewModel

@Composable
fun navGraph(authViewModel: AuthViewModel,viewModel: UserViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(route = Screen.Login.route) {
            Login(navController = navController, authViewModel)

        }

        composable(Screen.SignUp.route) {
            SignUP(navController = navController,authViewModel,viewModel)
        }
        composable(Screen.Home.route) {
            HomeScreen(viewModel)
        }
    }
}

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
}


