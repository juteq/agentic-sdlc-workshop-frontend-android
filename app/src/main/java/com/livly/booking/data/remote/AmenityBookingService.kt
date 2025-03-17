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

// Data classes for the API responses and requests

data class TimeSlot(
    val start: String,
    val end: String,
    val isAvailable: Boolean
)

data class CreateBookingCommand(
    val amenityId: String,
    val residentId: String,
    val startTime: String,
    val endTime: String
)
