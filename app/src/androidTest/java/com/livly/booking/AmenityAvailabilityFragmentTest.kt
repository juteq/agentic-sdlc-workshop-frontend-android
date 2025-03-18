package com.livly.booking

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.livly.booking.data.AmenityBookingRepository
import com.livly.booking.data.remote.TimeSlot
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AmenityAvailabilityFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    val repository: AmenityBookingRepository = mockk()

    @BindValue
    val featureFlagManager: FeatureFlagManager = mockk()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testAmenityAvailabilityDisplayed() {
        // Given
        val timeSlots = listOf(TimeSlot("2021-09-01T10:00:00", "2021-09-01T11:00:00", true))
        coEvery { repository.getAmenityAvailability(any(), any(), any()) } returns timeSlots
        every { featureFlagManager.isFeatureEnabled("amenity_availability") } returns true

        // When
        launchFragmentInContainer<AmenityAvailabilityFragment>()

        // Then
        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        onView(withText("2021-09-01T10:00:00")).check(matches(isDisplayed()))
        onView(withText("2021-09-01T11:00:00")).check(matches(isDisplayed()))
        onView(withText("Available")).check(matches(isDisplayed()))
    }

    @Test
    fun testAmenityAvailabilityError() {
        // Given
        coEvery { repository.getAmenityAvailability(any(), any(), any()) } throws Exception("Network error")
        every { featureFlagManager.isFeatureEnabled("amenity_availability") } returns true

        // When
        launchFragmentInContainer<AmenityAvailabilityFragment>()

        // Then
        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.recyclerView)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withText("Error: Network error")).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }
}
