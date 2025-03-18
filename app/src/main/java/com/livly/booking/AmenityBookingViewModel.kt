package com.livly.booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livly.booking.data.AmenityBookingRepository
import com.livly.booking.data.model.Result
import com.livly.booking.data.remote.model.TimeSlot
import com.livly.booking.data.remote.model.CreateBookingCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AmenityBookingViewModel @Inject constructor(
    private val repository: AmenityBookingRepository,
    private val featureFlagManager: FeatureFlagManager
) : ViewModel() {

    private val _amenityAvailability = MutableLiveData<Result<List<TimeSlot>>>()
    val amenityAvailability: LiveData<Result<List<TimeSlot>>> get() = _amenityAvailability

    private val _bookingResult = MutableLiveData<Result<String>>()
    val bookingResult: LiveData<Result<String>> get() = _bookingResult

    fun getAmenityAvailability(id: String, startDate: String?, endDate: String?) {
        if (featureFlagManager.isFeatureEnabled("amenity_availability")) {
            viewModelScope.launch {
                _amenityAvailability.value = Result.Loading
                try {
                    val availability = repository.getAmenityAvailability(id, startDate, endDate)
                    _amenityAvailability.value = Result.Success(availability)
                } catch (e: Exception) {
                    _amenityAvailability.value = Result.Error(e)
                }
            }
        }
    }

    fun createBooking(createBookingCommand: CreateBookingCommand) {
        if (featureFlagManager.isFeatureEnabled("create_booking")) {
            viewModelScope.launch {
                _bookingResult.value = Result.Loading
                try {
                    val result = repository.createBooking(createBookingCommand)
                    _bookingResult.value = Result.Success(result)
                } catch (e: Exception) {
                    _bookingResult.value = Result.Error(e)
                }
            }
        }
    }
}
