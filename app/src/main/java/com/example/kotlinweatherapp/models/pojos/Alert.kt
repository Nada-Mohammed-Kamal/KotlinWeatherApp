package com.example.kotlinweatherapp.models.pojos

data class Alert (
    val senderName: String,
    val event: String,
    val start: Long,
    val end: Long,
    val description: String,
    val tags: List<String>
)