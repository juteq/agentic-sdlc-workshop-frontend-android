package com.livly.booking.data

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
        return amenityBookingService.getAmenityAvailability(id, startDate, endDate)
    }

    suspend fun createBooking(createBookingCommand: CreateBookingCommand): String {
        return amenityBookingService.createBooking(createBookingCommand)
    }
}
