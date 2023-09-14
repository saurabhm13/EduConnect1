package com.example.educonnect.data

data class User(
    val userId: String,
    val username: String,
    val email: String? = null,
    val mobileNo: String? = null
)
