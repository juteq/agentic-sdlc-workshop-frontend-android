package com.livly.booking.data

import android.util.Log
import com.livly.booking.data.remote.AmenityBookingService
import com.livly.booking.data.remote.model.TimeSlot
import com.livly.booking.data.remote.model.CreateBookingCommand
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmenityBookingRepository @Inject constructor(
    private val amenityBookingService: AmenityBookingService
) {
    suspend fun getAmenityAvailability(id: String, startDate: String?, endDate: String?): List<TimeSlot> {
        Log.d("AmenityRepository", "Getting availability for amenity: $id")
        try {
            val result = amenityBookingService.getAmenityAvailability(id, startDate, endDate)
            Log.d("AmenityRepository", "Received ${result.size} time slots")
            return result
        } catch (e: Exception) {
            Log.e("AmenityRepository", "Error getting availability", e)
            throw e
        }
    }

    suspend fun createBooking(createBookingCommand: CreateBookingCommand): String {
        Log.d("AmenityRepository", "Creating booking: $createBookingCommand")
        try {
            val result = amenityBookingService.createBooking(createBookingCommand)
            Log.d("AmenityRepository", "Booking created with result: $result")
            return result
        } catch (e: Exception) {
            Log.e("AmenityRepository", "Error creating booking", e)
            throw e
        }
    }
}
