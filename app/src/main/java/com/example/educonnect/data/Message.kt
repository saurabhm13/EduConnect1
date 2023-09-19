package com.example.educonnect.data

data class Message(
    val message: String,
    val timeStamp: String,
    val senderId: String?
) {
    constructor() : this("", "", "")
}
