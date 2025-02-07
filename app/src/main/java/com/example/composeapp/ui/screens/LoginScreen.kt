package com.example.composeapp.ui.screens

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
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.composeapp.AuthViewModel
import com.example.composeapp.Screen
import com.example.composeapp.ui.tools.KeyboardAware


@Composable
fun Login(navController: NavHostController, authViewModel: AuthViewModel) {


    val authState = authViewModel.authState.observeAsState()
    KeyboardAware(){
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState())
    ) {
        Row(modifier = Modifier.padding(top = 95.dp, start = 25.dp)) {
            LoginTitle()
        }
        Box(Modifier.fillMaxSize().padding(bottom = 150.dp)) {
            CardView1(navController)
            CardView2()
            Column(Modifier.wrapContentSize()) {
                Row(
                    modifier = Modifier.padding(top = 30.dp, start = 170.dp),
                ) {
                    SignUpText()
                }
                Row(
                    modifier = Modifier.padding(top = 50.dp, start = 40.dp),
                ) {
                    WelcomeBackText()
                }
                Row(Modifier.padding(top = 30.dp)) {
                    IdPass(authViewModel,navController)
                }

            }
        }
    }
}
}

@Composable
fun LoginTitle( ) {
    Text(
        text = "LOGIN",
        color = Color.Blue,
        fontSize = 45.sp,
        fontWeight = FontWeight.SemiBold

    )
}
@Composable
fun WelcomeBackText(){
    Column {
        Text(
            text = "WELCOME BACK",
            fontSize = 35.sp
        )
        Text(
            text = "Please Enter Your Credentials"
            , fontSize = 20.sp,
            color = Color.LightGray
        )
    }
}

@Composable
 fun SignUpText(){
    Text(
        text = "SIGNUP",
        fontSize = 30.sp,
        modifier = Modifier
    )
}
@Composable
fun CardView1(navController: NavHostController){
    Card(
        modifier = Modifier.padding(top = 20.dp).size(width = 330.dp, height = 120.dp).rotate(-10f).clip(RoundedCornerShape(topEnd = 40.dp)).clickable {
            navController.navigate(Screen.SignUp.route){
                popUpTo(Screen.Login.route) { inclusive = true }
                launchSingleTop = true
            }
        },
        shape = RoundedCornerShape(topEnd = 40.dp),

    ) {}
}

@Composable
fun CardView2(){
    Card(
        modifier = Modifier.padding(top = 20.dp).size(width = 450.dp, height = 550.dp).rotate(8f).offset(
            (-50).dp,60.dp).shadow(10.dp, shape = RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp)),
        shape = RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp),
        border = BorderStroke(0.1.dp, Color.White),
        colors = CardDefaults.cardColors(Color.White)

    ){}

}


@Composable
fun IdPass(authViewModel:AuthViewModel,navController: NavHostController){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
    Column() {
        TextField(
            modifier = Modifier.padding(top = 15.dp).shadow(15.dp,
                RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp)
            ).width(300.dp),
            value = email,
            onValueChange = { email = it
                emailError = if (!emailRegex.matches(it)) "Invalid email format" else ""},
            label = { Text("Enter email")},
            isError = emailError.isNotEmpty(),
            shape = RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp),
            colors =  TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent
                , unfocusedIndicatorColor = Color.Transparent),
        )
        if (emailError.isNotEmpty()) {
            Text(text = emailError, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
        }

        TextField(
            modifier = Modifier.padding(top = 15.dp).shadow(15.dp,
                RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp)
            ).width(300.dp),
            value = password,
            onValueChange = { password = it
                passwordError = if (it.length < 6) "Password must be at least 6 characters"
                else if (!it.any { char -> char.isDigit() }) "Password must contain a number"
                else if (!it.any { char -> char.isUpperCase() }) "Password must contain an uppercase letter"
                else "" },
            label = { Text("Enter password") },

            shape = RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp),
            colors =  TextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
            isError = passwordError.isNotEmpty(),
            visualTransformation = PasswordVisualTransformation()
            )
        if (passwordError.isNotEmpty()) {
            Text(text = passwordError, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
        }
        Row(Modifier.padding(top = 35.dp, start = 40.dp)){
            Button(
                onClick = {
                    navController.navigate(Screen.Home.route){
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                    authViewModel.login(email , password)
                },
                colors = ButtonColors(
                    containerColor = Color.Blue,
                    Color.White,
                    Color.Gray,
                    Color.White
                ),
            ) {
                Text("Login",
                    fontSize = 20.sp)
            }
        }

    }
}




