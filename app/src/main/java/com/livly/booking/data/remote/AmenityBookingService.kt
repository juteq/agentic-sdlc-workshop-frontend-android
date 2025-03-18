package com.livly.booking.data.remote

import com.livly.booking.data.remote.model.TimeSlot
import com.livly.booking.data.remote.model.CreateBookingCommand
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AmenityBookingService {

    @GET("/api/Amenity/{id}/availability")
    suspend fun getAmenityAvailability(
        @Path("id") id: String,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): List<TimeSlot>

    @POST("/api/Booking")
    suspend fun createBooking(@Body createBookingCommand: CreateBookingCommand): String
}
