@ExtendWith(MockKExtension::class)
class AmenityBookingViewModelTest {

    @MockK
    private lateinit var repository: AmenityBookingRepository

    @MockK
    private lateinit var featureFlagManager: FeatureFlagManager

    private lateinit var viewModel: AmenityBookingViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = AmenityBookingViewModel(repository, featureFlagManager)
    }

    @Test
    fun `getAmenityAvailability should post value when feature is enabled`() = runBlockingTest {
        // Given
        val timeSlots = listOf(TimeSlot("2021-09-01T10:00:00", "2021-09-01T11:00:00", true))
        coEvery { repository.getAmenityAvailability(any(), any(), any()) } returns timeSlots
        every { featureFlagManager.isFeatureEnabled("amenity_availability") } returns true

        // When
        viewModel.getAmenityAvailability("sample-amenity-id", null, null)

        // Then
        assertEquals(timeSlots, viewModel.amenityAvailability.getOrAwaitValue())
    }

    @Test
    fun `createBooking should post value when feature is enabled`() = runBlockingTest {
        // Given
        val bookingResult = "Booking successful"
        coEvery { repository.createBooking(any()) } returns bookingResult
        every { featureFlagManager.isFeatureEnabled("create_booking") } returns true

        // When
        viewModel.createBooking(CreateBookingCommand("amenity-id", "resident-id", "2021-09-01T10:00:00", "2021-09-01T11:00:00"))

        // Then
        assertEquals(bookingResult, viewModel.bookingResult.getOrAwaitValue())
    }
}
