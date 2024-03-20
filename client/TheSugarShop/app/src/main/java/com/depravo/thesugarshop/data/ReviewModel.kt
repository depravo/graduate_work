package com.depravo.thesugarshop.data

data class ReviewModel(
    val user_fname: String,
    val user_lname: String,
    val rate: Int,
    val comment: String
)
