package com.depravo.thesugarshop.data

data class LoginCredentials(
    val email: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val userId: Int
)

data class RegisterCredentials(
    val email: String,
    val password: String,
    val fname: String,
    val lname: String,
    val phone_number: String
)

data class OperationResponse(
    val success: Boolean,
    val message: String,
    val userId: Int
)
