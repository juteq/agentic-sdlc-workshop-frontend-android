package com.livly.booking.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("Data")
    val data: Any?,
    @SerializedName("Error")
    val error: ErrorDetails?,
    @SerializedName("Success")
    val success: Boolean
)

data class ErrorDetails(
    @SerializedName("Message")
    val message: String?,
    @SerializedName("Error")
    val error: String?,
    @SerializedName("StatusCode")
    val statusCode: Int?
)