package com.example.educonnect.data

data class UserChats(
    val userId: String,
    val name: String,
    val image: String? = null,
    val timeStamp: String? = null,
    val lastMessage: String? = null,
    val status: Int? = null
) {
    constructor() : this("", "")
}

