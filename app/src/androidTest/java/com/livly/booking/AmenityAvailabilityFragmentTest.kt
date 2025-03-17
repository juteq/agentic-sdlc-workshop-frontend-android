@RunWith(AndroidJUnit4::class)
class AmenityAvailabilityFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testAmenityAvailabilityDisplayed() {
        // Given
        val timeSlots = listOf(TimeSlot("2021-09-01T10:00:00", "2021-09-01T11:00:00", true))
        val repository = mockk<AmenityBookingRepository> {
            coEvery { getAmenityAvailability(any(), any(), any()) } returns timeSlots
        }
        val featureFlagManager = mockk<FeatureFlagManager> {
            every { isFeatureEnabled("amenity_availability") } returns true
        }
        val viewModel = AmenityBookingViewModel(repository, featureFlagManager)

        // When
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))

        // Then
        onView(withText("2021-09-01T10:00:00")).check(matches(isDisplayed()))
        onView(withText("2021-09-01T11:00:00")).check(matches(isDisplayed()))
        onView(withText("Available")).check(matches(isDisplayed()))
    }
}
