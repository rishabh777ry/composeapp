package com.example.composeapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.composeapp.R
import com.example.composeapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

@Composable
fun HomeScreen(viewModel: UserViewModel, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    var currentUserName by remember { mutableStateOf("Loading...") }

    LaunchedEffect(currentUserId) {
        if (currentUserId.isNotEmpty()) {
            currentUserName = fetchCurrentUserName(db, currentUserId)
        }
    }

    Column {
        Text(
            text = "Welcome, $currentUserName 👋",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top=40.dp, start = 10.dp)
        )

        Box(Modifier.padding(start = 10.dp)) {
            SearchUser(navController, db, currentUserId)
        }

        RecentChatsScreen(db, currentUserId, navController)
    }
}

suspend fun fetchCurrentUserName(db: FirebaseFirestore, userId: String): String {
    return try {
        val snapshot = db.collection("users").document(userId).get().await()
        snapshot.getString("name") ?: "Unknown"
    } catch (e: Exception) {
        Log.e("Firestore", "Error fetching user name", e)
        "Unknown"
    }
}

@Composable
fun SearchUser(navController: NavController, db: FirebaseFirestore, currentUserId: String) {
    var query by remember { mutableStateOf("") }
    var users by remember { mutableStateOf<List<User>>(emptyList()) }

    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            users = fetchUsers(db, query)
        } else {
            users = emptyList()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        TextField(
            value = query,
            label = { Text("Search User") },
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .shadow(15.dp, RoundedCornerShape(40.dp)),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(40.dp),
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(users) { user ->
                UserItem(user) {
                    updateRecentChats(db, currentUserId, user)
                    navController.navigate("chat/${user.uid}")
                }
            }
        }
    }
}

@Composable
fun RecentChatsScreen(db: FirebaseFirestore, currentUserId: String, navController: NavController) {
    var recentChats by remember { mutableStateOf<List<Chat>>(emptyList()) }

    // Real-time snapshot listener
    LaunchedEffect(currentUserId) {
        val docRef = db.collection("users").document(currentUserId)
            .collection("chats")
            .orderBy("timestamp", Query.Direction.DESCENDING)

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("Firestore", "Error fetching chats", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val chatList = mutableListOf<Chat>()
                for (document in snapshot.documents) {
                    val chat = document.toObject(Chat::class.java)
                    if (chat != null) {
                        chatList.add(chat)
                    }
                }
                recentChats = chatList
            }
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(recentChats) { chat ->
            RecentChatRow(chat, navController)
        }
    }
}


@Composable
fun RecentChatRow(chat: Chat, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("chat/${chat.otherUserId}")
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.person),
            contentDescription = "Profile Photo",
            modifier = Modifier.size(50.dp).padding(8.dp),
            contentScale = ContentScale.Crop
        )

        Column {
            Text(text = chat.otherUserName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = chat.lastMessage, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun UserItem(user: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
    ) {
        Text(
            text = user.email,
            modifier = Modifier.padding(16.dp),
            fontSize = 16.sp
        )
    }
}

fun updateRecentChats(db: FirebaseFirestore, currentUserId: String, user: User) {
    val chatId = if (currentUserId < user.uid) "$currentUserId-${user.uid}" else "${user.uid}-$currentUserId"

    val chatData = mapOf(
        "chatId" to chatId,
        "participants" to listOf(currentUserId, user.uid),
        "otherUserId" to user.uid,
        "otherUserName" to user.email,
        "lastMessage" to "Say Hi!",
        "timestamp" to System.currentTimeMillis()
    )

    val userChatRef = db.collection("users").document(currentUserId)
        .collection("chats").document(user.uid)

    val otherUserChatRef = db.collection("users").document(user.uid)
        .collection("chats").document(currentUserId)

    userChatRef.set(chatData)
        .addOnSuccessListener { Log.d("Firestore", "Chat updated successfully for $currentUserId") }
        .addOnFailureListener { e -> Log.e("Firestore", "Error updating chat", e) }

    otherUserChatRef.set(chatData)
        .addOnSuccessListener { Log.d("Firestore", "Chat updated successfully for ${user.uid}") }
        .addOnFailureListener { e -> Log.e("Firestore", "Error updating chat", e) }
}

data class Chat(
    val chatId: String = "",
    val participants: List<String> = listOf(),
    val otherUserId: String = "",
    val otherUserName: String = "",
    val lastMessage: String = "",
    val timestamp: Long = 0
)

data class User(val uid: String = "", val email: String = "")

suspend fun fetchUsers(db: FirebaseFirestore, emailQuery: String): List<User> {
    return try {
        val result: QuerySnapshot = db.collection("users")
            .whereGreaterThanOrEqualTo("email", emailQuery)
            .whereLessThanOrEqualTo("email", emailQuery + '\uf8ff')
            .get()
            .await()
        result.documents.mapNotNull { it.toObject(User::class.java) }
    } catch (e: Exception) {
        Log.e("Firestore", "Error fetching users", e)
        emptyList()
    }
}
