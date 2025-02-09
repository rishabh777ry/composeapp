package com.example.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeapp.ui.screens.ChatScreen
import com.example.composeapp.ui.screens.HomeScreen
import com.example.composeapp.ui.screens.Login
import com.example.composeapp.ui.screens.SignUP
import com.example.composeapp.viewmodel.UserViewModel

@Composable
fun navGraph(authViewModel: AuthViewModel,viewModel: UserViewModel) {
    val navController = rememberNavController()

    val authState by authViewModel.authState.observeAsState()

    val startDestination = if (authState == AuthState.Authenticated) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable(route = Screen.Login.route) {
            Login(navController = navController, authViewModel)

        }
        composable(Screen.SignUp.route) {
            SignUP(navController = navController,authViewModel,viewModel)
        }
        composable(Screen.Home.route) {
            HomeScreen(viewModel,navController)
        }
        composable("chat/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ChatScreen( navController, userId)
        }
    }
}

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object Chat :Screen("chat")
}


