package com.example.composeapp.ui.screens


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.composeapp.AuthViewModel
import com.example.composeapp.Screen
import com.example.composeapp.data.entity.User
import com.example.composeapp.ui.tools.KeyboardAware
import com.example.composeapp.viewmodel.UserViewModel


@Composable
fun SignUP(navController: NavHostController, authViewModel: AuthViewModel,viewModel: UserViewModel) {
    KeyboardAware() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            SignUpTitle()
            SingUpContent(navController,authViewModel,viewModel)
        }
    }
}

@Composable
private fun SignUpTitle() {
    Row(Modifier.padding(top = 95.dp, start = 20.dp)) {
        Text(
            text = "SIGNUP",
            fontSize = 45.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Blue
        )
    }
}

@Composable
fun SingUpContent(navController: NavHostController, authViewModel: AuthViewModel,viewModel: UserViewModel) {

    val authState = authViewModel.authState.observeAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()

    Box(Modifier.fillMaxSize().padding(bottom = 150.dp)) {
        // Card for layout design
        Card(
            modifier = Modifier
                .padding(top = 20.dp)
                .size(width = 330.dp, height = 120.dp)
                .rotate(-10f)
                .clip(RoundedCornerShape(topEnd = 40.dp))
                .clickable {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            shape = RoundedCornerShape(topEnd = 40.dp)
        ) {}

        Card(
            modifier = Modifier
                .padding(top = 20.dp)
                .size(width = 450.dp, height = 550.dp)
                .rotate(8f)
                .offset((-50).dp, 60.dp)
                .shadow(10.dp, shape = RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp)),
            shape = RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp),
            border = BorderStroke(0.1.dp, Color.White),
            colors = CardDefaults.cardColors(Color.White)
        ) {}

        Column(modifier = Modifier) {
            Row(Modifier.padding(start = 170.dp, top = 30.dp)) {
                Text(
                    text = "LOGIN",
                    fontSize = 30.sp,
                )
            }
            Row(Modifier.padding(top = 30.dp, start = 20.dp)) {
                Text(
                    text = "Enter Details:",
                    fontSize = 25.sp
                )
            }

            // Name TextField
            TextField(
                modifier = Modifier
                    .padding(top = 35.dp)
                    .shadow(15.dp, RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp))
                    .width(300.dp),
                value = name,
                onValueChange = {
                    name = it
                    nameError = if (it.isEmpty()) "Name is required" else ""
                },
                label = { Text("Enter Name") },
                isError = nameError.isNotEmpty(),
                shape = RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            if (nameError.isNotEmpty()) {
                Text(text = nameError, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
            }

            // Email TextField
            TextField(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .shadow(15.dp, RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp))
                    .width(300.dp),
                value = email,
                onValueChange = {
                    email = it
                    emailError = if (!emailRegex.matches(it)) "Invalid email format" else ""
                },
                label = { Text("Enter email") },
                isError = emailError.isNotEmpty(),
                shape = RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            if (emailError.isNotEmpty()) {
                Text(text = emailError, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
            }

            // Password TextField
            TextField(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .shadow(15.dp, RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp))
                    .width(300.dp),
                value = password,
                onValueChange = {
                    password = it
                    passwordError = when {
                        it.length < 6 -> "Password must be at least 6 characters"
                        !it.any { char -> char.isDigit() } -> "Password must contain a number"
                        !it.any { char -> char.isUpperCase() } -> "Password must contain an uppercase letter"
                        else -> ""
                    }
                },
                label = { Text("Enter password") },
                isError = passwordError.isNotEmpty(),
                shape = RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            if (passwordError.isNotEmpty()) {
                Text(text = passwordError, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
            }

            // Confirm Password TextField
            TextField(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .shadow(15.dp, RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp))
                    .width(300.dp),
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = if (it != password) "Passwords do not match" else ""
                },
                label = { Text("Confirm Password") },
                isError = confirmPasswordError.isNotEmpty(),
                shape = RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            if (confirmPasswordError.isNotEmpty()) {
                Text(text = confirmPasswordError, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
            }

            // Sign Up Button
            Row(Modifier.padding(top = 55.dp, start = 55.dp)) {
                Button(onClick = {
                    val user = User(name = name, email = email)
                    viewModel.insertUser(user)
                    Log.d(">>//","here is the data of the user $user")

                    navController.navigate(Screen.Home.route){
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                 authViewModel.signup(email,password)
                },
                    colors = ButtonColors(
                        containerColor = Color.Blue,
                        Color.White,
                        Color.Gray,
                        Color.White
                    ),) {
                    Text("SignUp", fontSize = 20.sp)
                }
            }
        }
    }
}


//@Composable
//@Preview
//fun SignUpPreview(){
//    SignUP(navController)
//}