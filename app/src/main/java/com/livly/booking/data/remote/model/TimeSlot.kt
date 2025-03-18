package com.livly.booking.data.remote.model

data class TimeSlot(
    val start: String,
    val end: String,
    val isAvailable: Boolean
)