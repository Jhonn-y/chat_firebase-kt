package com.pedromoura.chatfirebase.presentation.chat

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pedromoura.chatfirebase.presentation.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(
    private val db: FirebaseDatabase,
    private val context: Context,
) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("log_prefs", Context.MODE_PRIVATE)

    var messagesText by mutableStateOf("")

    var username: String = ""
    var password: String = ""
    var userID: String = ""

    init {

        val messageRef = db.reference.child("messages")

        messageRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messagesList = mutableListOf<Message>()
                snapshot.children.forEach { child ->
                    val message = child.getValue(Message::class.java)
                    message?.let { messagesList.add(it) }
                }

                _messages.value = messagesList
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun onMessageTextChanged(text: String) {
        messagesText = text
    }

    fun sendMessage() {
        viewModelScope.launch {
            username = sharedPreferences.getString("USERNAME", "") ?: ""
            password = sharedPreferences.getString("PASSWORD", "") ?: ""
            userID = sharedPreferences.getString("USEID", "") ?: ""
        }

        val newMessage = Message(
            UUID.randomUUID().toString(),
            messagesText,
            userID ?: "",
            System.currentTimeMillis() / 1000L
        )

        db.reference.child("messages").push().setValue(newMessage)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }

        messagesText = ""
    }


}