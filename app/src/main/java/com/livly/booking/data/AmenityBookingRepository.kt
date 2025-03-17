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
