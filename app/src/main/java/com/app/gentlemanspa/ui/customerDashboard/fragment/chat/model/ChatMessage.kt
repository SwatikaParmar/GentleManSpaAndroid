package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.model

enum class MessageType {
    SENT, RECEIVED
}

data class ChatMessage(
    val sender: String,
    val message: String,
    val timestamp: String,
    val messageType: MessageType
)