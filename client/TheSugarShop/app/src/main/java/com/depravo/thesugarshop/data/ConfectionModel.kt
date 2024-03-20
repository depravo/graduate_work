package com.depravo.thesugarshop.data

data class ConfectionModel(
    val product_id: Int,
    val product_name: String,
    val img_resource: String,
    val description: String,
    val price: Float
)

data class FullConfectionModel(
    val product_id: Int,
    val product_name: String,
    val img_resource: String,
    val description: String,
    val price: Float,
    val rate: Float,
    val review: ArrayList<ReviewModel>
)
