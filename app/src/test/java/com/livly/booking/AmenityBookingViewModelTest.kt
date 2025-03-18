package com.livly.booking

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.livly.booking.data.AmenityBookingRepository
import com.livly.booking.data.remote.CreateBookingCommand
import com.livly.booking.data.remote.TimeSlot
import com.livly.booking.data.model.Result
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import io.mockk.junit5.MockKExtension

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class AmenityBookingViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var repository: AmenityBookingRepository

    @MockK
    private lateinit var featureFlagManager: FeatureFlagManager

    private lateinit var viewModel: AmenityBookingViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = AmenityBookingViewModel(repository, featureFlagManager)
    }

    @Test
    fun `getAmenityAvailability should post success result when feature is enabled`() = runTest {
        // Given
        val timeSlots = listOf(TimeSlot("2021-09-01T10:00:00", "2021-09-01T11:00:00", true))
        coEvery { repository.getAmenityAvailability(any(), any(), any()) } returns timeSlots
        every { featureFlagManager.isFeatureEnabled("amenity_availability") } returns true

        // When
        viewModel.getAmenityAvailability("sample-amenity-id", null, null)

        // Then
        assertEquals(Result.Success(timeSlots), viewModel.amenityAvailability.value)
    }

    @Test
    fun `getAmenityAvailability should post error result when repository throws exception`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { repository.getAmenityAvailability(any(), any(), any()) } throws exception
        every { featureFlagManager.isFeatureEnabled("amenity_availability") } returns true

        // When
        viewModel.getAmenityAvailability("sample-amenity-id", null, null)

        // Then
        assertEquals(Result.Error(exception), viewModel.amenityAvailability.value)
    }

    @Test
    fun `createBooking should post success result when feature is enabled`() = runTest {
        // Given
        val bookingResult = "Booking successful"
        val command = CreateBookingCommand("amenity-id", "resident-id", "2021-09-01T10:00:00", "2021-09-01T11:00:00")
        coEvery { repository.createBooking(command) } returns bookingResult
        every { featureFlagManager.isFeatureEnabled("create_booking") } returns true

        // When
        viewModel.createBooking(command)

        // Then
        assertEquals(Result.Success(bookingResult), viewModel.bookingResult.value)
    }
}
