package com.example.quickprintukm

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val fileName: String = "",
    val pageCount: Int = 1,
    val copies: Int = 1,
    val orientation: String = "",
    val paperSize: String = "",
    val color: String = "",
    val totalPrice: Double = 0.0,
    val collectOption: String = "",
    val status: String = "Submitted",
    val timestamp: Long = System.currentTimeMillis()
)

