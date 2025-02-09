package com.example.composeapp.ui.screens
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


@Composable
fun ChatScreen(navController: NavController, receiverId: String) {
    val db = FirebaseFirestore.getInstance()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    var messages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }
    var messageText by remember { mutableStateOf(TextFieldValue("")) }

    val chatId = getChatId(currentUserId, receiverId)
    val listState = rememberLazyListState()

    // **Auto-scroll to latest message when new message arrives**
    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // **Realtime Listener**
    DisposableEffect(chatId) {
        val query = db.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp")

        val listener = query.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("Firestore", "Error fetching messages", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                messages = snapshot.documents.mapNotNull { it.toObject(ChatMessage::class.java) }
            }
        }

        onDispose {
            listener.remove()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            items(messages) { message ->
                MessageBubble(message, isSentByMe = message.senderId == currentUserId)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                    .padding(8.dp)
            )
            Button(onClick = {
                if (messageText.text.isNotEmpty()) {
                    sendMessage(db, currentUserId, receiverId, messageText.text)
                    messageText = TextFieldValue("")
                }
            }) {
                Text("Send")
            }
        }
        Spacer(Modifier.height(35.dp))
    }
}

@Composable
fun MessageBubble(message: ChatMessage, isSentByMe: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = if (isSentByMe) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isSentByMe) Color.Green else Color.Gray,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(8.dp)
        ) {
            Text(
                text = message.text,
                color = Color.White
            )
        }
    }
}

// **Fixing Chat ID Logic**
fun getChatId(user1: String, user2: String): String {
    return if (user1 < user2) "$user1-$user2" else "$user2-$user1"
}
fun sendMessage(db: FirebaseFirestore, senderId: String, receiverId: String, text: String) {
    val chatId = getChatId(senderId, receiverId)
    val timestamp = System.currentTimeMillis()
    val message = mapOf(
        "senderId" to senderId,
        "receiverId" to receiverId,
        "text" to text,
        "timestamp" to timestamp
    )

    // **Message Firebase me store karna**
    db.collection("chats")
        .document(chatId)
        .collection("messages")
        .add(message)

    val lastMessageData = mapOf(
        "lastMessage" to text,
        "timestamp" to timestamp,
        "chatId" to chatId
    )

    // **Sender ke Home Screen me Last Message Update**
    db.collection("users").document(senderId)
        .collection("chats").document(receiverId)
        .set(lastMessageData, SetOptions.merge()) // 👈 Fix: Purana data delete nahi hoga

    // **Receiver ke Home Screen me Last Message Update**
    db.collection("users").document(receiverId)
        .collection("chats").document(senderId)
        .set(lastMessageData, SetOptions.merge())
}

data class ChatMessage(
    val text: String = "",
    val senderId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
