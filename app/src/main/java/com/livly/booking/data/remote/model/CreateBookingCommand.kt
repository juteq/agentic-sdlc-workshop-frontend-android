package com.livly.booking.data.remote.model

data class CreateBookingCommand(
    val amenityId: String,
    val residentId: String,
    val startTime: String,
    val endTime: String
)