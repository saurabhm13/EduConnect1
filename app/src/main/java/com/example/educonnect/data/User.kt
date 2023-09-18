package com.example.educonnect.data

data class User(
    val userId: String,
    val name: String,
    val email: String? = null,
    val mobileNo: String? = null,
    val image: String? = null,
)
